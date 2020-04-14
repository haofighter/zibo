package com.szxb.zibo.moudle.function.scan.freecode;

public class FreeScanEntity {


    /**
     * BlueRing : {"type":"Driver","value":"12345678"}
     */

    private BlueRingBean BlueRing;

    public BlueRingBean getBlueRing() {
        return BlueRing;
    }

    public void setBlueRing(BlueRingBean BlueRing) {
        this.BlueRing = BlueRing;
    }

    public static class BlueRingBean {
        /**
         * type : Driver
         * value : 12345678
         */

        private String type;
        private String value;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
