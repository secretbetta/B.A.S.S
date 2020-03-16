package com.secretbetta.BASS.PrinterService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

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

/**
 * <h1>Printer initializer (WIP)</h1>
 * Initializes a printer and prints files
 * 
 * @author Andrew
 */
public class PrinterInit {
	
	/**
	 * Prints a specific file name
	 * 
	 * @param filename Name of file to print
	 * @return True if successful, false otherwise (normally occurs if file not found or incompatible
	 *         DocFlavor/filetype)
	 */
	public static boolean printIt(String filename) {
		FileInputStream file = null;
		DocFlavor flavor = DocFlavor.INPUT_STREAM.PDF;
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		aset.add(MediaSizeName.ISO_A4);
		aset.add(OrientationRequested.PORTRAIT);
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
		
		try {
			file = new FileInputStream(filename);
			Doc myDoc = new SimpleDoc(file, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
			if (services.length > 0) {
				DocPrintJob job = null;
				for (PrintService service : services) {
					if (service.getName().contains("Brother")) {
						if (service.isDocFlavorSupported(flavor)) {
							job = service.createPrintJob();
						}
					}
				}
				job.print(myDoc, aset);
				return true;
			}
		} catch (FileNotFoundException e) {
			System.err.printf("File \"%s\" not found\n", "testutf.txt");
		} catch (PrintException e) {
			System.err.println("Could not print");
		}
		return false;
	}
}
