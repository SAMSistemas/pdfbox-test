import org.apache.jempbox.xmp.XMPMetadata;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;

/**
 * Created by santiago.pereztorre on 08/05/2015.
 */
public class WriteMetadata {

    public static void main(String[] args)
    {
        DocumentBuilderFactory f= DocumentBuilderFactory.newInstance();
        f.setExpandEntityReferences(true);
        f.setIgnoringComments(true);
        f.setIgnoringElementContentWhitespace(true);
        f.setValidating(false);
        f.setCoalescing(true);
        f.setNamespaceAware(true);
        DocumentBuilder builder= null;
        try {
            builder = f.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document xmpDoc = null;
        try {
            xmpDoc = builder.parse(new File(args[1]));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream in= null;
        try {
            in = new FileInputStream(args[0]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PDFParser parser= null;
        try {
            parser = new PDFParser(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            parser.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PDDocument document = null;
        try {
            document=  parser.getPDDocument();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PDDocumentCatalog cat = document.getDocumentCatalog();
        PDMetadata metadata = new PDMetadata(document);
        try {
            metadata.importXMPMetadata(new XMPMetadata(xmpDoc));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        cat.setMetadata(metadata);
        try {
            document.save(args[2]);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (COSVisitorException e) {
            e.printStackTrace();
        }


        InputStream in1= null;
        try {
            in1 = new FileInputStream(args[2]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PDFParser parser1= null;
        try {
            parser1 = new PDFParser(in1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            parser1.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PDMetadata metadata2 = null;
        try {
            metadata2 = parser.getPDDocument().getDocumentCatalog().getMetadata();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(metadata!=null)
        {
            try {
                System.out.println(metadata2.getInputStreamAsString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
