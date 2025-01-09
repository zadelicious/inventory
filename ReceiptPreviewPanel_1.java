import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JPanel;

public class ReceiptPreviewPanel_1 extends JPanel {
    private String orderId;
    private String customerName;
    private String cashierName;
    private List<String[]> items;
    private int totalPaid;
    private final SimpleDateFormat dateFormat;
    private final Date currentDate;

    public ReceiptPreviewPanel_1(String orderId, String customerName, String cashierName, 
                             List<String[]> items, int totalPaid) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.cashierName = cashierName;
        this.items = items;
        this.totalPaid = totalPaid;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.currentDate = new Date();
        
        // Set size for receipt preview (slightly larger than actual receipt for better visibility)
        setPreferredSize(new Dimension(300, 500)); // Increased width
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Using monospaced fonts with smaller size for preview
        Font titleFont = new Font("Monospaced", Font.BOLD, 16);
        Font headerFont = new Font("Monospaced", Font.BOLD, 12);
        Font regularFont = new Font("Monospaced", Font.PLAIN, 11);
        
        int lineHeight = 18;
        int startX = 10;
        int startY = 20;
        int currentY = startY;
        int width = getWidth() - 30;

        // Header - Store Name
        g2d.setFont(titleFont);
        String storeName = "INVENTORY MANAGEMENT";
        String storeSystem = "SYSTEM";
        FontMetrics titleMetrics = g2d.getFontMetrics();
        
        int storeNameX = (width - titleMetrics.stringWidth(storeName)) / 2;
        int storeSystemX = (width - titleMetrics.stringWidth(storeSystem)) / 2;
        
        g2d.drawString(storeName, storeNameX, currentY);
        currentY += lineHeight;
        g2d.drawString(storeSystem, storeSystemX, currentY);
        currentY += lineHeight;

        // Store Address
        g2d.setFont(regularFont);
        String[] storeInfo = {
            "123 Main Street, City",
            "Tel: (555) 123-4567",
            "===================================================="
        };

        for (String info : storeInfo) {
            int infoX = (width - g2d.getFontMetrics().stringWidth(info)) / 2;
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

        g2d.drawString("====================================================", startX, currentY);
        currentY += lineHeight;

        // Column Headers
        String itemHeader = String.format("%-20s %5s %8s %8s", "ITEM", "QTY", "PRICE", "TOTAL");
        g2d.drawString(itemHeader, startX, currentY);
        currentY += lineHeight;
        g2d.drawString("====================================================", startX, currentY);
        currentY += lineHeight;

        // Items
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
        g2d.drawString("====================================================", startX, currentY);
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
            "====================================================",
            dateFormat.format(currentDate)
        };

        for (String line : footer) {
            int lineX = (width - g2d.getFontMetrics().stringWidth(line)) / 2;
            g2d.drawString(line, lineX, currentY);
            currentY += lineHeight;
        }
    }

    private String truncateString(String str, int length) {
        if (str == null) return "";
        if (str.length() <= length) return str;
        return str.substring(0, length - 3) + "...";
    }
}