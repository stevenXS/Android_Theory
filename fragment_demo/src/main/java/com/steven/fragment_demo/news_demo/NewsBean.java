package com.steven.fragment_demo.news_demo;

/**
 * 通过Fragment实现在手机与平板显示不同内容：
 *  若是手机：则listview显示标题，点击标题再显示其内容；
 *  若是平板：通过listview左侧显示标题，右侧显示内容；
 */
public class NewsBean {
    private String title;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
