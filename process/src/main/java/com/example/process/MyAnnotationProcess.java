package com.example.process;

import com.example.annotation.MyBindView;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * 自定义注解解析器，实现将当前View自动绑定到activity：即activity.findViewById(int id)
 */
@AutoService(Processor.class) // 生成Java文件的注解
public class MyAnnotationProcess extends AbstractProcessor {
    public static final String TAG = "MyAnnotationProcess# ";
    /**
     * 文件相关的辅助类
     */
    private Filer mFiler;

    /**
     * 日志相关的辅助类
     */
    private Messager mMessager;

    /**
     * 绑定activity及view集合  一个activity对应多个view
     */
    private static final Map<TypeElement, List<ViewInfo>> viewMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mMessager = processingEnv.getMessager();
        mFiler = processingEnv.getFiler();
        loggerInfo(TAG + "BindViewProcess init");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations != null && !annotations.isEmpty()) {
            // 查看当前Java文件是否有自定义的注解类
            for (Element element : roundEnv.getElementsAnnotatedWith(MyBindView.class)) {
                if (element instanceof VariableElement) {
                    VariableElement variableElement = (VariableElement) element;
                    Set<Modifier> modifiers = variableElement.getModifiers(); // 权限修饰符
                    loggerInfo(TAG + modifiers.size() + " 个属性添加了MyBindView注解");
                    if (modifiers.contains(Modifier.PRIVATE) || modifiers.contains(Modifier.PROTECTED)) {
                        // 类型检查 ，如果是私有的，或者PROTECTED，则警告
                        mMessager.printMessage(Diagnostic.Kind.WARNING, "成员变量的类型不能是PRIVATE或者PROTECTED");
                    }
                    saveViewInfo(variableElement);
                }
            }
            // 保存所有注解数据后，对每个Activity里的View创建一个新的Java文件
            writeToFile();
            return true;
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        // 限制该处理器处理什么类型的注解
        LinkedHashSet<String> annos = new LinkedHashSet<>();
        annos.add(MyBindView.class.getCanonicalName());
        return annos;
    }

    // 保存每个activity对应的view集合
    private void saveViewInfo(VariableElement variableElement) {


        //获得当前元素variableElement的类型
        TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
        List<ViewInfo> viewInfos;
        if (viewMap.get(typeElement) != null) {
            // 如果不为null，说明该activity之前有关联的view，我们这时候把这个view集合拿出，再去把这个view也添加进去
            viewInfos = viewMap.get(typeElement);
        } else {
            // 没有就新创建一个
            viewInfos = new ArrayList<>();
        }
        // 这里会打印添加了对应注解的变量名
        loggerInfo(TAG + "[saveViewInfo] variableElement: " + variableElement  + ", typeElement " + typeElement);
        // 解析View注解上的信息并保存到集合
        MyBindView annotation = variableElement.getAnnotation(MyBindView.class);
        int viewId = annotation.value();  // 这里获取自定义注解的方法返回值
        String viewName = variableElement.getSimpleName().toString(); // 获取当前View元素的名称
        viewInfos.add(new ViewInfo(viewId, viewName));
        viewMap.put(typeElement, viewInfos);
        loggerInfo(TAG + "saveView: (viewId, viewName)" + viewId + " " + viewName );
    }

    /**
     * 生成文件：
     *  activity.textview = activity.findViewById(2131231103); 将这个过程自动化
     *
     * 编译完成后会在Android APP build/generated/ap_generated_sources/debug生成以下代码
     *  class AnnotationActivity_myBindView {
     *   public AnnotationActivity_myBindView(AnnotationActivity activity) {
     *     activity.textView = activity.findViewById(2131231174);
     *     activity.btn = activity.findViewById(2131230820);
     *   }
     * }
     */
    private void writeToFile() {
        Set<TypeElement> typeElements = viewMap.keySet();
        // 方法参数
        String paramName = "activity";
        loggerInfo(TAG + "viewlinkmap: "+ viewMap.size());
        for (TypeElement typeElement : typeElements) {
            ClassName className = ClassName.get(typeElement);//获取该元素对应的类的类型
            PackageElement packageElement = (PackageElement) typeElement.getEnclosingElement();//获得外部对象
            String packageName = packageElement.getQualifiedName().toString();//获得外部类的包名
            List<ViewInfo> viewInfos = viewMap.get(typeElement);
            loggerInfo(TAG + "typeElement " + typeElement+ ", className: " + className + ", packageName " + packageName + "\n");
            loggerInfo(TAG + "------------start generate code------------");
            // 定义方法体
            CodeBlock.Builder builder = CodeBlock.builder();
            CodeBlock.Builder commonBuilder = CodeBlock.builder();
            for (ViewInfo viewInfo : viewInfos) {
                // 等价于：activity.viewName = activity.findViewById(int viewId)
                builder.add(paramName + "." + viewInfo.getViewName() + " = " + paramName + ".findViewById(" + viewInfo.getViewId() + ");\n");
            }

            // 成员变量 这里不需要
//            FieldSpec fieldSpec = FieldSpec.builder(String.class, "name", Modifier.PRIVATE).build();

            // 添加构造方法，这里表示生成的是构造方法
            /**
             * public ClassName_ParamName()
             */
            builder.add("printMsg(\"1111111111111\");\n");
            MethodSpec methodSpec = MethodSpec.constructorBuilder()//生成的方法对象
                    .addModifiers(Modifier.PUBLIC)//方法的修饰符
                    .addParameter(className, paramName)//方法中的参数，第一个是参数类型（例：MainActivity），第二个是参数名
                    .addCode(builder.build())// 方法体的代码
                    .build();

//            // 添加普通方法
            commonBuilder.add("android.util.Log.d(\"aa#\", msg);");
            MethodSpec commonMethod = MethodSpec.methodBuilder("printMsg")//生成的方法对象
                    .addModifiers(Modifier.PUBLIC)//方法的修饰符
                    .addParameter(String.class, "msg")//方法中的参数，第一个是参数类型（例：MainActivity），第二个是参数名
                    .addCode(commonBuilder.build())//方法体重的代码
                    .build();

            // 生成类
            TypeSpec typeSpec = TypeSpec.classBuilder(typeElement.getSimpleName().toString() + MyBindView.SUFFIX)//类对象，参数：类名
                    .addMethod(methodSpec)//添加方法
                    .addMethod(commonMethod) // 添加方法二
//                    .addField(fieldSpec)//添加成员变量，我们这里不需要
                    .build();

            // 构建文件并指定生成的文件目录, 参数1：包名；参数2：TypeSpec
            JavaFile javaFile = JavaFile.builder(packageName, typeSpec).build();

            try {
                javaFile.writeTo(mFiler);//写入文件
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断一个类型是否为Activity
     *
     * class: com.apt.viewlinkiddemo.BaseActivity
     * class: androidx.appcompat.app.AppCompatActivity
     * class: androidx.fragment.app.FragmentActivity
     * class: androidx.activity.ComponentActivity
     * class: androidx.core.app.ComponentActivity
     * class: android.app.Activity
     * @param typeMirror  获得外部元素对象的类路径 例：com.apt.viewlinkiddemo.MainActivity
     * @return
     */
    private boolean isActivity(TypeMirror typeMirror) {
        Types typeUtils = processingEnv.getTypeUtils();
        List<? extends TypeMirror> typeMirrors = typeUtils.directSupertypes(typeMirror);
        for (TypeMirror mirror : typeMirrors) {
            loggerInfo("class: "+mirror.toString());
            if ("android.app.Activity".equals(mirror.toString())) {
                return true;
            }
            if (isActivity(mirror)) {
                return true;
            }
        }
        return false;
    }

    public void loggerInfo(String msg) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, msg);
    }
}
