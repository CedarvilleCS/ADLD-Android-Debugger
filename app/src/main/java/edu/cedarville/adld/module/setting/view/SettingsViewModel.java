package edu.cedarville.adld.module.setting.view;

public class SettingsViewModel {


    //------------------------------------------------------------------------------
    // View Attributes
    //------------------------------------------------------------------------------
    private final String robotName;
    private final String robotAddress;
    private final boolean displayHex;
    private final String threshold;



    //------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------
    private SettingsViewModel(Builder builder) {
        robotName = builder.robotName;
        robotAddress = builder.robotAddress;
        displayHex = builder.displayHex;
        threshold = builder.threshold;
    }



    //------------------------------------------------------------------------------
    // Getters
    //------------------------------------------------------------------------------
    public String getRobotName() {
        return robotName;
    }

    public String getRobotAddress() {
        return robotAddress;
    }

    public boolean isDisplayHex() {
        return displayHex;
    }

    public String getThreshold() {
        return threshold;
    }



    //------------------------------------------------------------------------------
    // Class Builder
    //------------------------------------------------------------------------------
    public static final class Builder {
        private String robotName;
        private String robotAddress;
        private boolean displayHex;
        private String threshold;

        public Builder() {
        }

        public Builder withRobotName(String val) {
            robotName = val;
            return this;
        }

        public Builder withRobotAddress(String val) {
            robotAddress = val;
            return this;
        }

        public Builder withDisplayHex(boolean val) {
            displayHex = val;
            return this;
        }

        public Builder withThreshold(String val) {
            threshold = val;
            return this;
        }

        public Builder withThreshold(int val) {
            return this.withThreshold(Integer.toString(val));
        }

        public SettingsViewModel build() {
            return new SettingsViewModel(this);
        }
    }
}
