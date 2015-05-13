import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by martin.montenegro on 13/05/2015.
 */
public class ReadPDFContent {

    /**
     * args[0]: Path + Filename
     * @param args
     */
    public static void main(String[] args) {

        PDFTextStripper pdfTextStripper;
        try
        {
            // Cargar documento
            PDDocument pdDocument = PDDocument.load(args[0]);

            // Strippeamos el texto
            pdfTextStripper = new PDFTextStripper();
            String texto = pdfTextStripper.getText(pdDocument);

            // Creamos patron regex del CUIL
            Pattern patronCUIL = Pattern.compile("(20|23|27)-[0-9]{8}-[0-9]{1}");
            Matcher m = patronCUIL.matcher(texto);
            while (m.find()) {
                String s = m.group(0);
                System.out.println(s);
            }

            System.out.println("---");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
