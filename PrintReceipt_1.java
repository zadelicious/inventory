import java.awt.*;
import java.awt.print.*;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PrintReceipt_1 implements Printable {

    private String orderId;
    private String customerName;
    private String cashierName;
    private List<String[]> items;
    private int totalPaid;
    private final SimpleDateFormat dateFormat;
    private final Date currentDate;

    public PrintReceipt_1(String orderId, String customerName, String cashierName, List<String[]> items, int totalPaid) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.cashierName = cashierName;
        this.items = items;
        this.totalPaid = totalPaid;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.currentDate = new Date();
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Standard thermal receipt size (80mm × variable length)
        Paper paper = new Paper();
        double width = 283.46; // 100mm in points (1 mm = 2.83465 points)
        double height = 566.93; // 200mm in points
        paper.setSize(width, height);
        paper.setImageableArea(0, 0, width, height);
        pageFormat.setPaper(paper);

        // Using monospaced fonts typical in thermal receipts
        Font titleFont = new Font("Monospaced", Font.BOLD, 14);
        Font headerFont = new Font("Monospaced", Font.BOLD, 10);
        Font regularFont = new Font("Monospaced", Font.PLAIN, 8);

        int lineHeight = 13;
        int startX = 10;
        int startY = 10;
        int currentY = startY;
        int width_margin = (int) width - 20;

        // Header - Store Name
        g2d.setFont(titleFont);
        String storeName = "INVENTORY MANAGEMENT";
        String storeSystem = "SYSTEM";
        FontMetrics titleMetrics = g2d.getFontMetrics();

        int storeNameX = (width_margin - titleMetrics.stringWidth(storeName)) / 2;
        int storeSystemX = (width_margin - titleMetrics.stringWidth(storeSystem)) / 2;

        g2d.drawString(storeName, storeNameX, currentY);
        currentY += lineHeight;
        g2d.drawString(storeSystem, storeSystemX, currentY);
        currentY += lineHeight;

        // Store Address
        g2d.setFont(regularFont);
        String[] storeInfo = {
            "123 Main Street, City",
            "Tel: (555) 123-4567",
            "===================================="
        };

        for (String info : storeInfo) {
            int infoX = (width_margin - g2d.getFontMetrics().stringWidth(info)) / 2;
            g2d.drawString(info, infoX, currentY);
            currentY += lineHeight;
        }

        // Transaction Details
        g2d.setFont(regularFont);
        g2d.drawString("Date    : " + dateFormat.format(currentDate), startX, currentY);
        currentY += lineHeight;
        g2d.drawString("Order # : " + orderId, startX, currentY);
        currentY += lineHeight;
        g2d.drawString("Cashier : " + cashierName, startX, currentY);
        currentY += lineHeight;
        g2d.drawString("Customer: " + customerName, startX, currentY);
        currentY += lineHeight;

        g2d.drawString("====================================", startX, currentY);
        currentY += lineHeight;

        String itemHeader = String.format("%-20s %5s %8s %8s", "ITEM", "QTY", "PRICE", "TOTAL");
        g2d.drawString(itemHeader, startX, currentY);
        currentY += lineHeight;
        g2d.drawString("====================================", startX, currentY);
        currentY += lineHeight;

        // Items - adjust the spacing to match header
        for (String[] item : items) {
            String itemLine = String.format("%-20s %5s %8s %8s",
                    truncateString(item[0], 20),
                    item[2],
                    "₱" + item[1],
                    "₱" + item[3]);
            g2d.drawString(itemLine, startX, currentY);
            currentY += lineHeight;
        }

        // Total
        g2d.drawString("====================================", startX, currentY);
        currentY += lineHeight;
        g2d.setFont(headerFont);
        String total = String.format("TOTAL: ₱ %d", totalPaid);
        g2d.drawString(total, startX, currentY);
        currentY += lineHeight * 2;

        // Footer
        g2d.setFont(regularFont);
        String[] footer = {
            "Thank you for your purchase!",
            "Please visit again",
            "",
            "* Keep receipt for any returns *",
            "Returns accepted within 7 days",
            "with original receipt",
            "====================================",
            dateFormat.format(currentDate)
        };

        for (String line : footer) {
            int lineX = (width_margin - g2d.getFontMetrics().stringWidth(line)) / 2;
            g2d.drawString(line, lineX, currentY);
            currentY += lineHeight;
        }

        return PAGE_EXISTS;
    }

    private String truncateString(String str, int length) {
        if (str == null) {
            return "";
        }
        if (str.length() <= length) {
            return str;
        }
        return str.substring(0, length - 3) + "...";
    }

    public PageFormat getPageFormat() {
        PageFormat pageFormat = new PageFormat();
        Paper paper = new Paper();

        // Standard thermal receipt dimensions
        double width = 283.46; // 100mm
        double height = 566.93; // 200mm

        paper.setSize(width, height);
        paper.setImageableArea(0, 0, width, height);

        pageFormat.setPaper(paper);
        pageFormat.setOrientation(PageFormat.PORTRAIT);

        return pageFormat;
    }
}