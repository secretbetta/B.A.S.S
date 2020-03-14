import java.awt.print.PrinterJob;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;

import org.apache.pdfbox.pdmodel.PDDocument;

public class PrintPS {
	
	public static void main(String[] args) {
		// PrintPS test = new PrintPS();
	}
	
	public void printer() throws FileNotFoundException, IOException {
		PDDocument doc = PDDocument.load(new FileInputStream(System.getProperty("java.io.tmpdir") + "\\pdf.pdf")); // read
																													// pdf
																													// file.
		String printerNameDesired = "VENDOR THERMAL PRINTER";
		
		javax.print.PrintService[] service = PrinterJob.lookupPrintServices();
		DocPrintJob docPrintJob = null;
		
		int count = service.length;
		for (int i = 0; i < count; i++) {
			if (service[i].getName().equalsIgnoreCase(printerNameDesired)) {
				docPrintJob = service[i].createPrintJob();
				i = count;
			}
		}
		
		PrinterJob pjob = PrinterJob.getPrinterJob();
		// pjob.setPrintService(docPrintJob.getPrintService());
		pjob.setJobName("job");
		// doc.silentPrint(pjob);
		
	}
	
	public PrintPS() {
		FileInputStream file = null;
		
		try {
			file = new FileInputStream("test.pdf");
		} catch (FileNotFoundException e) {
			System.err.printf("File \"%s\" not found\n", "testutf.txt");
		}
		
		// DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
		DocFlavor flavor = DocFlavor.INPUT_STREAM.POSTSCRIPT;
		
		Doc myDoc = new SimpleDoc(file, flavor, null);
		// Doc myDoc = new SimpleDoc(file, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		aset.add(MediaSizeName.NA_LETTER);
		aset.add(OrientationRequested.PORTRAIT);
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
		if (services.length > 0) {
			DocPrintJob job = null;
			PDDocument doc = null;
			try {
				doc = PDDocument.load(file);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			for (PrintService service : services) {
				if (service.getName().contains("Brother")) {
					if (service.isDocFlavorSupported(flavor)) {
						job = service.createPrintJob();
						System.out.println(job.getPrintService().getName());
					} else {
						DocFlavor[] flavors = service.getSupportedDocFlavors();
						for (DocFlavor f : flavors) {
							System.out.println(f.getMediaSubtype());
						}
						System.out.println(service.getUnsupportedAttributes(flavor, aset));
					}
				}
			}
			try {
				PrinterJob j = PrinterJob.getPrinterJob();
				job.print(myDoc, aset);
				System.out.println("Printed at: " + job.getPrintService().getName());
			} catch (PrintException e) {
				System.err.println("Could not print");
			}
		}
	}
	
	public static PrintService findPrintService(String printerName) {
		PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
		for (PrintService printService : printServices) {
			if (printService.getName().trim().equals(printerName)) {
				return printService;
			}
		}
		return null;
	}
}
