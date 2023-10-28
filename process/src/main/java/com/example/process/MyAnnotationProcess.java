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
 * 自定义注解解析器，实现绑定ViewID
 */
@AutoService(Processor.class) // 生成Java文件的注解
public class MyAnnotationProcess extends AbstractProcessor {
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
        System.out.println("BindViewProcess init");
        mMessager = processingEnv.getMessager();
        mFiler = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // 处理注解
        for (Element element : roundEnv.getElementsAnnotatedWith(MyBindView.class)) {
            if (element instanceof VariableElement) {
                VariableElement variableElement = (VariableElement) element;
                Set<Modifier> modifiers = variableElement.getModifiers(); // 权限修饰符
                if (modifiers.contains(Modifier.PRIVATE) || modifiers.contains(Modifier.PROTECTED)) {
                    // 类型检查 ，如果是私有的，或者PROTECTED，则退出
                    mMessager.printMessage(Diagnostic.Kind.ERROR, "成员变量的类型不能是PRIVATE或者PROTECTED");
                    return false;
                }
                saveViewInfo(variableElement);
            }
        }
        // 保存所有注解数据后，对每个Activity里的View创建一个新的Java文件
        writeToFile();
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
        System.out.println("variableElement: " + variableElement.toString());
        //获得外部元素对象: xxx.MainActivity
        TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
        List<ViewInfo> viewInfos;
        if (viewMap.get(typeElement) != null) {
            // 如果不为null，说明该activity之前有关联的view，我们这时候把这个view集合拿出，再去把这个view也添加进去
            viewInfos = viewMap.get(typeElement);
        } else {
            // 没有就新创建一个
            viewInfos = new ArrayList<>();
        }
        MyBindView annotation = variableElement.getAnnotation(MyBindView.class);
        int viewId = annotation.value();
        String viewName = variableElement.getSimpleName().toString();
        viewInfos.add(new ViewInfo(viewId, viewName));
        viewMap.put(typeElement, viewInfos);
        System.out.println("saveView: (viewId, viewName)" + viewId + " " + viewName );
    }

    /**
     * 生成文件
     */
    private void writeToFile() {
        Set<TypeElement> typeElements = viewMap.keySet();
        // 方法参数
        String paramName = "activity";
        System.out.println("viewlinkmap: "+ viewMap.size());
        for (TypeElement typeElement : typeElements) {
            ClassName className = ClassName.get(typeElement);//获取参数类型
            PackageElement packageElement = (PackageElement) typeElement.getEnclosingElement();//获得外部对象
            String packageName = packageElement.getQualifiedName().toString();//获得外部类的包名 例：com.apt.viewlinkiddemo
            List<ViewInfo> viewInfos = viewMap.get(typeElement);
            //代码块对象   就是这玩意：activity.textview = activity.findViewById(2131231103);
            //                      activity.bt = activity.findViewById(2131230807);
            CodeBlock.Builder builder = CodeBlock.builder();
            for (ViewInfo viewInfo : viewInfos) {
                //***********************生成代码**************************
                builder.add(paramName + "." + viewInfo.getViewName() + " = " + paramName + ".findViewById(" + viewInfo.getViewId() + ");\n");
            }

            // 成员变量 我们这里不需要
//            FieldSpec fieldSpec = FieldSpec.builder(String.class, "name", Modifier.PRIVATE).build();

            // 添加构造方法
            MethodSpec methodSpec = MethodSpec.constructorBuilder()//生成的方法对象
                    .addModifiers(Modifier.PUBLIC)//方法的修饰符
                    .addParameter(className, paramName)//方法中的参数，第一个是参数类型（例：MainActivity），第二个是参数名
                    .addCode(builder.build())//方法体重的代码
                    .build();

//            // 添加普通方法
//            MethodSpec methodSpec2 = MethodSpec.methodBuilder("commonMethodName")//生成的方法对象
//                    .addModifiers(Modifier.PUBLIC)//方法的修饰符
//                    .addParameter(className, paramName)//方法中的参数，第一个是参数类型（例：MainActivity），第二个是参数名
//                    .addCode(builder.build())//方法体重的代码
//                    .build();
            // 添加类
            TypeSpec typeSpec = TypeSpec.classBuilder(typeElement.getSimpleName().toString() + MyBindView.SUFFIX)//类对象，参数：类名
                    .addMethod(methodSpec)//添加方法
//                    .addMethod(methodSpec2) // 添加方法二
//                    .addField(fieldSpec)//添加成员变量，我们这里不需要
                    .build();

            //javaFile对象，最终用来写入的对象，参数1：包名；参数2：TypeSpec
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
            System.out.println("class: "+mirror.toString());
            if ("android.app.Activity".equals(mirror.toString())) {
                return true;
            }
            if (isActivity(mirror)) {
                return true;
            }
        }
        return false;
    }
}
