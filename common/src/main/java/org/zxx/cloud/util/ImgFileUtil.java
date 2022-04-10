package org.zxx.cloud.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.imageio.ImageIO;

public class ImgFileUtil {

    /*******************************************************************************
     * 缩略图类（通用） 本java类能将jpg、bmp、png、gif图片文件，进行等比或非等比的大小转换。 具体使用方法
     * compressPic(大图片路径,生成小图片路径,大图片文件名,生成小图片文名,生成小图片宽度,生成小图片高度,是否等比缩放(默认为true))
     */
    private File file = null; // 文件对象
    private String inputDir; // 输入图路径
    private String outputDir; // 输出图路径
    private String inputFileName; // 输入图文件名
    private String outputFileName; // 输出图文件名
    private int outputWidth = 100; // 默认输出图片宽
    private int outputHeight = 100; // 默认输出图片高
    private boolean proportion = true; // 是否等比缩放标记(默认为等比缩放)

    public ImgFileUtil() { // 初始化变量
        inputDir = "";
        outputDir = "";
        inputFileName = "";
        outputFileName = "";
        outputWidth = 100;
        outputHeight = 100;
    }

    public void setInputDir(String inputDir) {
        this.inputDir = inputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public void setOutputWidth(int outputWidth) {
        this.outputWidth = outputWidth;
    }

    public void setOutputHeight(int outputHeight) {
        this.outputHeight = outputHeight;
    }

    public void setWidthAndHeight(int width, int height) {
        this.outputWidth = width;
        this.outputHeight = height;
    }

    /*
     * 获得图片大小 传入参数 String path ：图片路径
     */
    public long getPicSize(String path) {
        file = new File(path);
        return file.length();
    }

    // 图片处理
    public String compressPic() {
        try {
            // 获得源文件
            file = new File(inputDir + inputFileName);
            //System.out.println(inputDir + inputFileName);
            if (!file.exists()) {
                System.out.println(" can't read file " + inputDir + inputFileName);
                return "no";
            }
            Image img = ImageIO.read(file);
            // 判断图片格式是否正确
            if (img.getWidth(null) == -1) {
                System.out.println(" can't read,retry!" + "<BR>");
                return "no";
            } else {
                int newWidth;
                int newHeight;
                // 判断是否是等比缩放
                if (this.proportion == true) {
                    // 为等比缩放计算输出的图片宽度及高度
                    double rate1 = ((double) img.getWidth(null))
                            / (double) outputWidth + 0.1;
                    double rate2 = ((double) img.getHeight(null))
                            / (double) outputHeight + 0.1;
                    // 根据缩放比率大的进行缩放控制
                    double rate = rate1 > rate2 ? rate1 : rate2;
                    newWidth = (int) (((double) img.getWidth(null)) / rate);
                    newHeight = (int) (((double) img.getHeight(null)) / rate);
                } else {
                    newWidth = outputWidth; // 输出的图片宽度
                    newHeight = outputHeight; // 输出的图片高度
                }
                BufferedImage tag = new BufferedImage((int) newWidth,
                        (int) newHeight, BufferedImage.TYPE_INT_RGB);

                /*
                 * Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
                 */
                tag.getGraphics().drawImage(
                        img.getScaledInstance(newWidth, newHeight,
                                Image.SCALE_SMOOTH), 0, 0, null);
                // 加水印
                Graphics2D g = tag.createGraphics();
                //g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
                float alpha = 0.2f; // 透明度
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
                Color color = Color.red;
                g.setColor(color);     //根据图片的背景设置水印颜色
                Font font = new Font("黑体", Font.BOLD, 36);
                g.setFont(font);              //设置字体
                String waterMarkContent = "图片仅限个人物品报关使用";  //水印内容
                // 一行14个位置，超出部分进行换行
                int page = (int) Math.ceil((double) waterMarkContent.length() / 8);
                for (int i = 0; i < page; i++) {
                    String sub = null;
                    if ((i + 1) * 8 < waterMarkContent.length()) {
                        sub = waterMarkContent.substring(i * 8, (i + 1) * 8);
                    } else {
                        sub = waterMarkContent.substring(i * 8, waterMarkContent.length());
                    }
                    g.drawString(sub, newWidth / 2 - 200, newHeight / 2 - 60 + i * 42); // 画文字

                }
                g.dispose();

                if (outputDir != null && !"".equals(outputDir)) {
                    File f = new File(outputDir);
                    if (!f.exists()) {
                        f.mkdirs();
                    }
                }
                FileOutputStream out = new FileOutputStream(outputDir
                        + outputFileName);
                // JPEGImageEncoder可适用于其他图片类型的转换
//                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//                encoder.encode(tag);
                ImageIO.write(tag, "jpg", out);
                out.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return "no";
        }
        return "ok";
    }

    public String compressPic(String outputDir, String inputFileName,
                              String outputFileName) {
        // 输出图路径
        this.outputDir = outputDir;
        // 输入图文件名
        this.inputFileName = inputFileName;
        // 输出图文件名
        this.outputFileName = outputFileName;
        return compressPic();
    }

    public String compressPic(String outputDir, String inputFileName,
                              String outputFileName, int size) {
        if (outputDir.lastIndexOf("/") != (outputDir.length() - 1)) {
            outputDir += "/";
        }

        // 输出图路径
        this.inputDir = outputDir;
        this.outputDir = outputDir;
        // 输入图文件名
        this.inputFileName = inputFileName;
        // 输出图文件名
        this.outputFileName = outputFileName;

        this.outputHeight = size;
        this.outputWidth = size;

        return compressPic();
    }

    public String compressPic(String inputDir, String outputDir,
                              String inputFileName, String outputFileName, int width, int height,
                              boolean gp) {
        // 输入图路径
        this.inputDir = inputDir;
        // 输出图路径
        this.outputDir = outputDir;
        // 输入图文件名
        this.inputFileName = inputFileName;
        // 输出图文件名
        this.outputFileName = outputFileName;
        // 设置图片长宽
        setWidthAndHeight(width, height);
        // 是否是等比缩放 标记
        this.proportion = gp;
        return compressPic();
    }

    // ###################
    private static final int numOfEncAndDec = 0x99; //加密解密秘钥
    private static int dataOfFile = 0; //文件字节内容

    public static void main(String[] args) {

        File srcFile = new File("C:\\Users\\Admin\\Desktop\\src.png"); //初始文件
        File encFile = new File("C:\\Users\\Admin\\Desktop\\src_enc.png"); //加密文件
        File decFile = new File("C:\\Users\\Admin\\Desktop\\src_dec.png"); //解密文件

        try {
            EncFile(srcFile, encFile); //加密操作
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            DecFile(encFile, decFile); //加密操作
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("aaa");
        String t=encoderOrdecoder("aaa", Cipher.ENCRYPT_MODE );
        System.out.println(t);
        System.out.println(encoderOrdecoder(t, Cipher.DECRYPT_MODE ));

       t= encoderOrdecoder("C:\\Users\\Admin\\Desktop","src.png", Cipher.ENCRYPT_MODE);
        System.out.println("file::::1111:"+t);
        System.out.println("file::::2222:"+encoderOrdecoder("C:\\Users\\Admin\\Desktop","src.png", Cipher.DECRYPT_MODE));
    }

    private static final String PASSKEY = "afasdf";
    private static final String DESKEY = "asfsdfsdf";
    /**
     * @Comments ：对文件进行加密
     * @param filePath  要加密的文件路径
     * @param fileName 文件
     * @param mode 加密模式  加密：Cipher.ENCRYPT_MODE 解密：Cipher.DECRYPT_MODE
     * @return
     */
    public static String encoderOrdecoder(String filePath, String fileName, int mode) {

        InputStream is = null;
        OutputStream out = null;
        CipherInputStream cis = null;
        try {
            SecureRandom sr = new SecureRandom();
            DESKeySpec dks = new DESKeySpec(DESKEY.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(dks);
            IvParameterSpec iv = new IvParameterSpec(PASSKEY.getBytes());
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(mode, securekey, iv, sr);

            File encoderFile = new File(filePath + File.separator + "encoder");
            if (!encoderFile.exists()) {
                encoderFile.mkdir();
            }

            is = new FileInputStream(filePath + File.separator + fileName);
            out = new FileOutputStream(filePath + File.separator + "encoder"
                    + File.separator + fileName);

            cis = new CipherInputStream(is, cipher);
            byte[] buffer = new byte[1024];
            int r;
            while ((r = cis.read(buffer)) > 0) {
                out.write(buffer, 0, r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (cis != null) {
                    cis.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e1){

            }
        }
        return filePath + File.separator + "encoder" + File.separator
                + fileName;
    }

    /**@Comments ：对字符串进行加密
     * @param src 源字符串
     * @param mode 加密模式  加密：Cipher.ENCRYPT_MODE 解密：Cipher.DECRYPT_MODE
     * @return
     */
    public static String encoderOrdecoder( String src, int mode) {
        String tag="";
        InputStream is = null;
        OutputStream out = null;
        CipherInputStream cis = null;

        try {
            SecureRandom sr = new SecureRandom();
            DESKeySpec dks = new DESKeySpec(DESKEY.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(dks);
            IvParameterSpec iv = new IvParameterSpec(PASSKEY.getBytes());
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(mode, securekey, iv, sr);
            cis = new CipherInputStream(new ByteArrayInputStream(src.getBytes()) , cipher);
            out=new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int r;
            while ((r = cis.read(buffer)) > 0) {
                out.write(buffer, 0, r);
            }
            tag=out.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (cis != null) {
                    cis.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e1){

            }
        }
        return tag;
    }
    private static void EncFile(File srcFile, File encFile) throws Exception {
        if (!srcFile.exists()) {
            System.out.println("source file not exixt");
            return;
        }

        if (!encFile.exists()) {
            System.out.println("encrypt file created");
            encFile.createNewFile();
        }
        InputStream fis = new FileInputStream(srcFile);
        OutputStream fos = new FileOutputStream(encFile);

        while ((dataOfFile = fis.read()) > -1) {
            fos.write(dataOfFile ^ numOfEncAndDec);
        }

        fis.close();
        fos.flush();
        fos.close();
    }

    private static void DecFile(File encFile, File decFile) throws Exception {
        if (!encFile.exists()) {
            System.out.println("encrypt file not exixt");
            return;
        }

        if (!decFile.exists()) {
            System.out.println("decrypt file created");
            decFile.createNewFile();
        }

        InputStream fis = new FileInputStream(encFile);
        OutputStream fos = new FileOutputStream(decFile);

        while ((dataOfFile = fis.read()) > -1) {
            fos.write(dataOfFile ^ numOfEncAndDec);
        }

        fis.close();
        fos.flush();
        fos.close();
    }


}