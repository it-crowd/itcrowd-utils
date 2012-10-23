package pl.itcrowd.utils.test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class WebClientUtils {
// ------------------------------ FIELDS ------------------------------

    private static long defaultCheckInterval = 500;

    private static long defaultTimeout = 10000;

// -------------------------- STATIC METHODS --------------------------

    /**
     * Checks if a specified cell in given table contains searched text.
     *
     * @param table        HtmlTable element in which text is searched.
     * @param cellNumber   Number of column of the table in which text is searched.
     * @param searchedText Text to be searched
     *
     * @return True if text was found, false otherwise.
     */
    public static boolean contains(HtmlTable table, int cellNumber, String searchedText)
    {
        List<HtmlTableRow> rows = getTableRows(table);
        for (HtmlTableRow row : rows) {
            if (row.getCell(cellNumber).getTextContent().contains(searchedText)) {
                return true;
            }
        }
        return false;
    }

    public static void executeAjaxReRenderedScripts(HtmlPage page)
    {
        final DomNodeList<HtmlElement> scripts = page.getElementsByTagName("script");
        /**
         * We cannot iterate over html DomNodeList cause it depends on sibling relationship which we will modify.
         */
        final List<HtmlElement> scriptsList = new ArrayList<HtmlElement>();
        for (HtmlElement element : scripts) {
            scriptsList.add(element);
        }
        for (HtmlElement element : scriptsList) {
            if (element.getChildNodes().size() > 1) {
                element.removeChild(element.getFirstChild());
                final DomNode sibling = element.getNextSibling();
                final DomNode parentNode = element.getParentNode();
                /**
                 * Script will be executed upon inserting into DOM tree, so we removed and add it again.
                 */
                if (sibling != null) {
                    parentNode.removeChild(element);
                    sibling.insertBefore(element);
                } else {
                    parentNode.removeChild(element);
                    parentNode.appendChild(element);
                }
            }
        }
    }

    public static void forceWait(int timeout)
    {
        final long startTime = System.currentTimeMillis();
        do {
            try {
                final long millis = startTime + timeout - System.currentTimeMillis();
                if (millis > 0) {
                    Thread.sleep(millis);
                }
            } catch (InterruptedException ignore) {
                //This is hack for checkstyle
                //noinspection UnnecessaryContinue
                continue;
            } catch (IllegalArgumentException ignore) {
                //This is hack for checkstyle
                //noinspection UnnecessaryContinue
                continue;
            }
        } while (startTime + timeout > System.currentTimeMillis());
    }

    public static long getDefaultCheckInterval()
    {
        return defaultCheckInterval;
    }

    public static long getDefaultTimeout()
    {
        return defaultTimeout;
    }

    /**
     * IMPORTANT: Because of bugs in htmlunit and jsfunit which make schedule component render improperly this method should be used only to retrieve
     * cell elements from the first row of schedule grid. Any operations on cell elements from other rows are likely to cause error.
     *
     * @param schedule   schedule element
     * @param dayOfMonth number of day of month representing cell you wish to retrieve
     *
     * @return list of schedule cells
     */
    public static HtmlTableDataCell getScheduleDayCell(HtmlElement schedule, int dayOfMonth)
    {
        return (HtmlTableDataCell) schedule.getByXPath(".//td[contains(@class,'fc-day') and not(contains(@class,'fc-other-month'))]").get(dayOfMonth - 1);
    }

    @SuppressWarnings("unchecked")
    public static List<HtmlSpan> getScheduleEventTitles(HtmlElement schedule)
    {
        return (List<HtmlSpan>) schedule.getByXPath(".//*[@class='fc-event-title']");
    }

    /**
     * Returns list of suggestions from rich:suggestionBox
     *
     * @param suggestion suggestionBox element
     * @param column     column of suggestionBox to extract text from
     *
     * @return list of suggestions
     */
    public static Map<String, HtmlTableCell> getSuggestions(HtmlElement suggestion, int column)
    {
        final Map<String, HtmlTableCell> suggestions = new HashMap<String, HtmlTableCell>();
        final HtmlElement suggestElement = suggestion.getElementById(suggestion.getId() + ":suggest");
        @SuppressWarnings("unchecked")
        final DomNodeList<HtmlElement> suggestionRows = suggestElement.getElementsByTagName("tr");
        for (HtmlElement row : suggestionRows) {
            if (!row.getId().endsWith("NothingLabel")) {
                @SuppressWarnings("unchecked")
                final DomNodeList<HtmlElement> cells = row.getElementsByTagName("td");
                final HtmlTableCell cell = (HtmlTableCell) cells.get(column + 1);
                suggestions.put(cell.asText(), cell);
            }
        }
        return suggestions;
    }

    @SuppressWarnings("unchecked")
    public static List<HtmlTableRow> getTableRows(HtmlTable table)
    {
        return (List<HtmlTableRow>) table.getByXPath(".//*[contains(@class,'rf-dt-r')]");
    }

    public static void setDefaultCheckInterval(long defaultCheckInterval)
    {
        WebClientUtils.defaultCheckInterval = defaultCheckInterval;
    }

    public static void setDefaultTimeout(long defaultTimeout)
    {
        WebClientUtils.defaultTimeout = defaultTimeout;
    }

    public static int waitForJSJob(String message, WebClient webClient)
    {
        return waitForJSJob(message, webClient, webClient.waitForBackgroundJavaScript(10) - 1);
    }

    public static int waitForJSJob(String message, WebClient webClient, int initialJobCount)
    {
        return waitForJSJob(message, webClient, initialJobCount, defaultTimeout);
    }

    public static int waitForJSJob(WebClient webClient, int initialJobCount, int timeout)
    {
        return waitForJSJob(null, webClient, initialJobCount, timeout);
    }

    public static int waitForJSJob(String message, WebClient webClient, int initialJobCount, long timeout)
    {
        return waitForJSJob(message, webClient, initialJobCount, timeout, defaultCheckInterval);
    }

    public static int waitForJSJob(String message, WebClient webClient, int initialJobCount, int timeout)
    {
        return waitForJSJob(message, webClient, initialJobCount, timeout, defaultCheckInterval);
    }

    public static int waitForJSJob(String message, WebClient webClient, int initialJobCount, long timeout, long checkInterval)
    {
        int jobs;
        long startTime = System.currentTimeMillis();
        do {
            jobs = webClient.waitForBackgroundJavaScript(checkInterval);
            if (startTime + timeout < System.currentTimeMillis()) {
                throw new RuntimeException("Number of JavaScript jobs doesn't drop to initial level for " + timeout
                    + " seconds. It's memory leak in your JavaScript rather then request taking so long!");
            }
        } while (jobs > initialJobCount);
        System.out.println("Waiting" + (message == null ? "" : " for " + message) + " took: " + (System.currentTimeMillis() - startTime) + "ms");
        return jobs;
    }

// --------------------------- CONSTRUCTORS ---------------------------

    private WebClientUtils()
    {
    }
}
