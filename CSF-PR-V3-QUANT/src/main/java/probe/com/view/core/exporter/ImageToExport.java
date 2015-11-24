/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core.exporter;

import java.io.Serializable;

/**
 *
 * @author yfa041
 */
public class ImageToExport implements Serializable {

    private int width;
    private int height;
    private byte[]imgByteArr;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public byte[] getImgByteArr() {
        return imgByteArr;
    }

    public void setImgByteArr(byte[] imgByteArr) {
        this.imgByteArr = imgByteArr;
    }

}
