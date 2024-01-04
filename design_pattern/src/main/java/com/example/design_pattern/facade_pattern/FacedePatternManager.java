package com.example.design_pattern.facade_pattern;

/**
 * 外观模式类：
 *  外观模式：将一些子系统的行为进一步封装，屏蔽子系统的细节；系统需要分层设计时考虑外观模式，例如做饭前、做饭后分别做什么，这种分层场景可考虑外观模式
 */
public class FacedePatternManager {
    private DvDPlayer dvDPlayer;
    private Film film;
    private Screen screen;

    public FacedePatternManager() {
        this.dvDPlayer = DvDPlayer.getInstance();
        this.film = Film.getInstance();
        this.screen = Screen.getInstance();
    }

    public void ready() {
        this.screen.on();
    }

    public void play() {
        this.film.on();
        this.dvDPlayer.on();
    }

    public void end() {
        this.dvDPlayer.off();
        this.film.off();
        this.screen.off();
    }
}
