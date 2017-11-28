/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ultra;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Comp {

    static Boolean typ;

    public static void decom(String infile, String outfile) throws Exception {
        /*System.out.println("Decompressing " + infile + " to " + outfile);
         *long byteCount = 0;
         */
        FileOutputStream out;
        try  {
            GZIPInputStream in = new GZIPInputStream(new FileInputStream(infile));
            out = new FileOutputStream(outfile);
            byte[] buf = new byte[16384];
            int read;
            while ((read = in.read(buf)) != -1) {
                out.write(buf, 0, read);
                //byteCount += read;
            }
            out.close();
        } catch (Exception ex) {
            System.out.println("File " + infile + " not found!!");
        }
        /*File unzipped = new File(infile);
         *System.out.println("read "+ unzipped.length() + " bytes");
         *System.out.println("wrote "+ byteCount + " bytes");
         */
    }

    public static void com(String srcfile, String dstfile) {
        GZIPOutputStream fout;
        try{
            FileInputStream fin = new FileInputStream(srcfile);
            fout = new GZIPOutputStream(new FileOutputStream(dstfile));
            byte[] buffer = new byte[16384];
            int bytesRead;
            while ((bytesRead = fin.read(buffer)) != -1) //srcfile.getBytes()
            {
                fout.write(buffer, 0, bytesRead);
            }
            fout.close();
            /*File file = new File(srcfile);
             * System.out.println("Before Compression file Size :" + file.length() + " Bytes");
             * File file1 = new File(dstfile);
             * System.out.println("After Compression file Size :" + file1.length() + " Bytes");
             */
        } catch (Exception ex) {
            System.out.println("File " + srcfile + " not found!!");
        }


    }

    public static void foldr(String dir, String dst) throws Exception {
        String nam;
        File folder;
        folder = new File(dir);
        if (!folder.exists()) {
            System.out.println("File/Folder " + dir + " not found!!");
            return;
        }
        if (folder.isFile()) {
            nam = folder.getName();
            if (Comp.typ) {
                com(dir, dst + "/" + nam + ".gzp");
            } else {
                if (".gzp".equals(nam.substring(nam.lastIndexOf("."), nam.length()))) {
                    //check n decompress only files compressed by this program(.gzp extension)
                    decom(dir, dst + "/" + nam.substring(0, nam.lastIndexOf(".")));
                }
            }

        } else {
            dst += dir.substring(dir.lastIndexOf("/"), dir.length());
            File nf = new File(dst);

            if (!nf.isDirectory()) {
                nf.mkdir();
            }
            File[] listOfFiles = folder.listFiles();
            if (listOfFiles == null) {
                return; //if folder is not directory listoffiles gets null
            }
            for (File file : listOfFiles) {

                nam = file.getName();
                if (file.isFile()) {
                    if (Comp.typ) {
                        com(dir + "/" + nam, dst + "/" + nam + ".gzp");
                    } else {
                        if (".gzp".equals(nam.substring(nam.lastIndexOf("."), nam.length()))) {
                            //check n decompress only files compressed by this program(.gzp extension)
                            decom(dir + "/" + nam, dst + "/" + nam.substring(0, nam.lastIndexOf(".")));
                        }
                    }
                } else {
                    foldr(dir + "/" + nam, dst);
                }
            }
        }
    }

    public static void fol(List<String> dir, String dst) {
        for (String s : dir) {
            try {
                foldr(s, dst);
            } catch (Exception ex) {
                System.out.println("File error for file/folder " + s + "!!");
            }
        }
    }

    public static void main(String[] args) {

        /*
         * encoding example
         * e D:/test D:/test.pdf D:/java D:/test.txt D:/compressed
         * 
         * decoding example
         * d D:/compressed D:/orignal
         */
        if (args.length < 3) {
            System.out.println("Incorrect Arguments Format\nUsage: \n[e/d] [folder/file names seprated by spaces] [output folder adress]\nencoding example\ne D:/test D:/test.pdf D:/java D:/test.txt D:/compressed\n\ndecoding example\nd D:/compressed D:/orignal ");
            return;
        }
		if("e".equals(args[0]))
		{        
			Comp.typ = true;
        }
		else if("d".equals(args[0]))
		{
            Comp.typ = false;
        }
		else
		{
			System.out.println("Incorrect encoding/decoding Argument\nUsage: \n[e/d] [folder/file names seprated by spaces] [output folder adress]\nencoding example\ne D:/test D:/test.pdf D:/java D:/test.txt D:/compressed\n\ndecoding example\nd D:/compressed D:/orignal");
        }

        List<String> s = new ArrayList<String>();
        for (int i = 1; i < args.length - 1; i++) {
            s.add(args[i]);
        }
        File folder = new File(args[args.length - 1]);
        if (!folder.isDirectory()) {
            folder.mkdir();
        }

        fol(s, args[args.length - 1]);
    }
}
