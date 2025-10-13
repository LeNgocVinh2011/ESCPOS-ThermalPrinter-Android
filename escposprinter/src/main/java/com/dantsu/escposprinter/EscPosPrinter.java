package com.dantsu.escposprinter;

import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.exceptions.EscPosBarcodeException;
import com.dantsu.escposprinter.exceptions.EscPosConnectionException;
import com.dantsu.escposprinter.exceptions.EscPosEncodingException;
import com.dantsu.escposprinter.exceptions.EscPosParserException;
import com.dantsu.escposprinter.textparser.PrinterTextParser;
import com.dantsu.escposprinter.textparser.PrinterTextParserColumn;
import com.dantsu.escposprinter.textparser.IPrinterTextParserElement;
import com.dantsu.escposprinter.textparser.PrinterTextParserLine;
import com.dantsu.escposprinter.textparser.PrinterTextParserString;
import android.graphics.Bitmap;
import android.graphics.Color;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Vector;

public class EscPosPrinter extends EscPosPrinterSize {

    private EscPosPrinterCommands printer = null;

    /**
     * Create new instance of EscPosPrinter.
     *
     * @param printerConnection           Instance of class which implement DeviceConnection
     * @param printerDpi                  DPI of the connected printer
     * @param printerWidthMM              Printing width in millimeters
     * @param printerNbrCharactersPerLine The maximum number of characters that can be printed on a line.
     */
    public EscPosPrinter(DeviceConnection printerConnection, int printerDpi, float printerWidthMM, int printerNbrCharactersPerLine) throws EscPosConnectionException {
        this(printerConnection != null ? new EscPosPrinterCommands(printerConnection) : null, printerDpi, printerWidthMM, printerNbrCharactersPerLine);
    }

    /**
     * Create new instance of EscPosPrinter.
     *
     * @param printerConnection           Instance of class which implement DeviceConnection
     * @param printerDpi                  DPI of the connected printer
     * @param printerWidthMM              Printing width in millimeters
     * @param printerNbrCharactersPerLine The maximum number of characters that can be printed on a line.
     * @param charsetEncoding             Set the charset encoding.
     */
    public EscPosPrinter(DeviceConnection printerConnection, int printerDpi, float printerWidthMM, int printerNbrCharactersPerLine, EscPosCharsetEncoding charsetEncoding) throws EscPosConnectionException {
        this(printerConnection != null ? new EscPosPrinterCommands(printerConnection, charsetEncoding) : null, printerDpi, printerWidthMM, printerNbrCharactersPerLine);
    }

    /**
     * Create new instance of EscPosPrinter.
     *
     * @param printer                     Instance of EscPosPrinterCommands
     * @param printerDpi                  DPI of the connected printer
     * @param printerWidthMM              Printing width in millimeters
     * @param printerNbrCharactersPerLine The maximum number of characters that can be printed on a line.
     */
    public EscPosPrinter(EscPosPrinterCommands printer, int printerDpi, float printerWidthMM, int printerNbrCharactersPerLine) throws EscPosConnectionException {
        super(printerDpi, printerWidthMM, printerNbrCharactersPerLine);
        if (printer != null) {
            this.printer = printer.connect();
        }
    }

    /**
     * Close the connection with the printer.
     *
     * @return Fluent interface
     */
    public EscPosPrinter disconnectPrinter() {
        if (this.printer != null) {
            this.printer.disconnect();
            this.printer = null;
        }
        return this;
    }

    /**
     * Active "ESC *" command for image printing.
     *
     * @param enable true to use "ESC *", false to use "GS v 0"
     * @return Fluent interface
     */
    public EscPosPrinter useEscAsteriskCommand(boolean enable) {
        this.printer.useEscAsteriskCommand(enable);
        return this;
    }

    /**
     * Print a formatted text. Read the README.md for more information about text formatting options.
     *
     * @param text Formatted text to be printed.
     * @return Fluent interface
     */
    public EscPosPrinter printFormattedText(String text) throws EscPosConnectionException, EscPosParserException, EscPosEncodingException, EscPosBarcodeException {
        return this.printFormattedText(text, 20f);
    }

    /**
     * Print a formatted text. Read the README.md for more information about text formatting options.
     *
     * @param text        Formatted text to be printed.
     * @param mmFeedPaper millimeter distance feed paper at the end.
     * @return Fluent interface
     */
    public EscPosPrinter printFormattedText(String text, float mmFeedPaper) throws EscPosConnectionException, EscPosParserException, EscPosEncodingException, EscPosBarcodeException {
        return this.printFormattedText(text, this.mmToPx(mmFeedPaper));
    }

    /**
     * Print a formatted text. Read the README.md for more information about text formatting options.
     *
     * @param text          Formatted text to be printed.
     * @param dotsFeedPaper distance feed paper at the end.
     * @return Fluent interface
     */
    public EscPosPrinter printFormattedText(String text, int dotsFeedPaper) throws EscPosConnectionException, EscPosParserException, EscPosEncodingException, EscPosBarcodeException {
        if (this.printer == null || this.printerNbrCharactersPerLine == 0) {
            return this;
        }

        PrinterTextParser textParser = new PrinterTextParser(this);
        PrinterTextParserLine[] linesParsed = textParser
                .setFormattedText(text)
                .parse();

        this.printer.reset();

        for (PrinterTextParserLine line : linesParsed) {
            PrinterTextParserColumn[] columns = line.getColumns();

            IPrinterTextParserElement lastElement = null;
            for (PrinterTextParserColumn column : columns) {
                IPrinterTextParserElement[] elements = column.getElements();
                for (IPrinterTextParserElement element : elements) {
                    element.print(this.printer);
                    lastElement = element;
                }
            }

            if (lastElement instanceof PrinterTextParserString) {
                this.printer.newLine();
            }
        }

        this.printer.feedPaper(dotsFeedPaper);
        return this;
    }

    /**
     * Print a formatted text and cut the paper. Read the README.md for more information about text formatting options.
     *
     * @param text Formatted text to be printed.
     * @return Fluent interface
     */
    public EscPosPrinter printFormattedTextAndCut(String text) throws EscPosConnectionException, EscPosParserException, EscPosEncodingException, EscPosBarcodeException {
        return this.printFormattedTextAndCut(text, 20f);
    }

    /**
     * Print a formatted text and cut the paper. Read the README.md for more information about text formatting options.
     *
     * @param text        Formatted text to be printed.
     * @param mmFeedPaper millimeter distance feed paper at the end.
     * @return Fluent interface
     */
    public EscPosPrinter printFormattedTextAndCut(String text, float mmFeedPaper) throws EscPosConnectionException, EscPosParserException, EscPosEncodingException, EscPosBarcodeException {
        return this.printFormattedTextAndCut(text, this.mmToPx(mmFeedPaper));
    }

    /**
     * Print a formatted text and cut the paper. Read the README.md for more information about text formatting options.
     *
     * @param text          Formatted text to be printed.
     * @param dotsFeedPaper distance feed paper at the end.
     * @return Fluent interface
     */
    public EscPosPrinter printFormattedTextAndCut(String text, int dotsFeedPaper) throws EscPosConnectionException, EscPosParserException, EscPosEncodingException, EscPosBarcodeException {
        if (this.printer == null || this.printerNbrCharactersPerLine == 0) {
            return this;
        }

        this.printFormattedText(text, dotsFeedPaper);
        this.printer.cutPaper();

        return this;
    }

    /**
     * Print a formatted text, cut the paper and open the cash box. Read the README.md for more information about text formatting options.
     *
     * @param text        Formatted text to be printed.
     * @param mmFeedPaper millimeter distance feed paper at the end.
     * @return Fluent interface
     */
    public EscPosPrinter printFormattedTextAndOpenCashBox(String text, float mmFeedPaper) throws EscPosConnectionException, EscPosParserException, EscPosEncodingException, EscPosBarcodeException {
        return this.printFormattedTextAndOpenCashBox(text, this.mmToPx(mmFeedPaper));
    }

    /**
     * Print a formatted text, cut the paper and open the cash box. Read the README.md for more information about text formatting options.
     *
     * @param text          Formatted text to be printed.
     * @param dotsFeedPaper distance feed paper at the end.
     * @return Fluent interface
     */
    public EscPosPrinter printFormattedTextAndOpenCashBox(String text, int dotsFeedPaper) throws EscPosConnectionException, EscPosParserException, EscPosEncodingException, EscPosBarcodeException {
        if (this.printer == null || this.printerNbrCharactersPerLine == 0) {
            return this;
        }

        this.printFormattedTextAndCut(text, dotsFeedPaper);
        this.printer.openCashBox();
        return this;
    }

    /**
     * @return Charset encoding
     */
    public EscPosCharsetEncoding getEncoding() {
        return this.printer.getCharsetEncoding();
    }


    /**
     * Print all characters of all charset encoding
     *
     * @return Fluent interface
     */
    public EscPosPrinter printAllCharsetsEncodingCharacters() {
        this.printer.printAllCharsetsEncodingCharacters();
        return this;
    }

    /**
     * Print all characters of selected charsets encoding
     *
     * @param charsetsId Array of charset id to print.
     * @return Fluent interface
     */
    public EscPosPrinter printCharsetsEncodingCharacters(int[] charsetsId) {
        this.printer.printCharsetsEncodingCharacters(charsetsId);
        return this;
    }

    /**
     * Print all characters of a charset encoding
     *
     * @param charsetId Charset id to print.
     * @return Fluent interface
     */
    public EscPosPrinter printCharsetEncodingCharacters(int charsetId) {
        this.printer.printCharsetEncodingCharacters(charsetId);
        return this;
    }

    public EscPosPrinter printImageAndCut(Bitmap bitmap) throws EscPosConnectionException, IOException {
        if (this.printer == null || this.printerNbrCharactersPerLine == 0) {
            return this;
        }
        // Scale bitmap width to 80mm
        int w = 576;
        int h = bitmap.getHeight() * w / bitmap.getWidth();
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, w, h, true);

        int bytesPerLine = (w + 7) / 8;
        byte[] image = new byte[bytesPerLine * h];
        int[] pixels = new int[w * h];
        scaled.getPixels(pixels, 0, w, 0, 0, w, h);

        // Convert image to binary image
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int color = pixels[y * w + x];
                int gray = ((color >> 16) & 0xFF) + ((color >> 8) & 0xFF) + (color & 0xFF);
                if (gray < 480) image[y * bytesPerLine + (x >> 3)] |= (byte) (0x80 >> (x & 7));
            }
        }

        // Command print fast image
        ByteArrayOutputStream cmd = new ByteArrayOutputStream();
        cmd.write(new byte[]{0x1D, 0x76, 0x30, 0x00});
        cmd.write(bytesPerLine & 0xFF);
        cmd.write((bytesPerLine >> 8) & 0xFF);
        cmd.write(h & 0xFF);
        cmd.write((h >> 8) & 0xFF);
        cmd.write(image);

        // print image
        this.printer.printImage(cmd.toByteArray());
        this.printer.cutPaper();

        return this;
    }

    public EscPosPrinter printTscLabel(Bitmap bitmap) throws EscPosConnectionException {
        TscCommand tsc = new TscCommand();
        tsc.addSize(50, 30);
        tsc.addGap(2);
        tsc.addCls();
        tsc.addBitmap(0, 0, TscCommand.BITMAP_MODE.OVERWRITE, bitmap.getWidth(), bitmap);
        tsc.addPrint(1, 1);

        Vector<Byte> command = tsc.getCommand();
        byte[] bytes = new byte[command.size()];
        for (int i = 0; i < command.size(); i++) {
            bytes[i] = command.get(i);
        }

        this.printer.printImage(bytes);
        return this;
    }
}
