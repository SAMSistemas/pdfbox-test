import java.io.*;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.apache.pdfbox.pdfparser.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;


public class ReadMetadata
{
    static private void extract(InputStream in)
            throws Exception
    {
        PDDocument document=null;
        try
        {
            PDFParser parser=new PDFParser(in);
            parser.parse();
            document= parser.getPDDocument();
            if(document.isEncrypted())
            {
                System.err.println("Document is Encrypted!");
            }
            PDDocumentCatalog cat = document.getDocumentCatalog();
            PDMetadata metadata = cat.getMetadata();
            if(metadata!=null)
            {
                //System.out.println(metadata.getStream().getStreamTokens());

                // Levantamos la MetaData
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(new ByteArrayInputStream(metadata.getInputStreamAsString().getBytes()));

                // Buscamos el tag de SEmployee y el Element -> CUIT
                NodeList nList = doc.getElementsByTagName("foaf:SEmployee");
                Element elem = (Element) nList.item(0);
                String cuit = elem.getElementsByTagName("foaf:cuit").item(0).getTextContent();

                System.out.println(cuit);


                System.out.println("---");
                System.out.println(metadata.getInputStreamAsString());
            }
        }
        catch(Exception err)
        {
            throw err;
        }
        finally
        {
            if(document!=null) try { document.close();} catch(Throwable err2) {}
        }
    }

    static public void main(String args[])
    {
        try
        {
            int optind=0;
            while(optind<args.length)
            {
                if(args[optind].equals("-h"))
                {
                    System.err.println("Pierre Lindenbaum PhD. 2010");
                    System.err.println("-h this screen");
                    System.err.println("pdf1 pdf2 pdf3 ....");
                    return;
                }
                else if (args[optind].equals("--"))
                {
                    ++optind;
                    break;
                }
                else if (args[optind].startsWith("-"))
                {
                    System.err.println("bad argument " + args[optind]);
                    System.exit(-1);
                }
                else
                {
                    break;
                }
                ++optind;
            }
            if(optind==args.length)
            {
                extract(System.in);
            }
            else
            {
                while(optind< args.length)
                {
                    String filename=args[optind++];
                    InputStream in=new FileInputStream(filename);
                    extract(in);
                    in.close();
                }
            }


        }
        catch(Throwable err)
        {
            err.printStackTrace();
        }
    }
}