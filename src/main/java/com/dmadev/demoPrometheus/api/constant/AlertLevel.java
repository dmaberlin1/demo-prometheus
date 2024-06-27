package com.dmadev.demoPrometheus.api.constant;

import lombok.Getter;

@Getter
public enum AlertLevel {
    NONE("NONE", 0),
    GREEN("GREEN", 1),
    YELLOW("YELLOW", 2),
    RED("RED", 3),
    BLACK("BLACK", 4);

    private final String text;
    private final int level;

    AlertLevel(String text, int level) {
        this.text = text;
        this.level = level;
    }

    public static AlertLevel fromText(String text) {
        for (AlertLevel level : values()) {
            if (level.text.equalsIgnoreCase(text)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Unknown Alertlevel text: "+text);
    }

    public  static AlertLevel fromLevel(int level){

        for (AlertLevel alertLevel :values() ) {
            if(alertLevel.level == level){
                return alertLevel;
            }
        }
        throw new IllegalArgumentException("Unknown AlertLevel level: "+ level);
    }


}
