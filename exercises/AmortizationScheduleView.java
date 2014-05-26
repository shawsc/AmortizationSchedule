package exercises;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.IllegalFormatException;

/**
 * Created by Steven on 5/25/2014.
 */


public class AmortizationScheduleView {

    private static Console console = System.console();

    protected BigDecimal amountBorrowed;        // in cents
    protected BigDecimal apr;
    protected BigDecimal initialTermYears;

    protected double borrowAmountMinimum;
    protected double borrowAmountMaximum;
    protected double borrowAPRMinimum;
    protected double borrowAPRMaximum;
    protected int borrowTermMinimum;
    protected int borrowTermMaximum;


    {
        amountBorrowed = BigDecimal.ZERO;
        apr = BigDecimal.ZERO;
        initialTermYears = BigDecimal.ZERO;
        borrowAmountMinimum = 0;
        borrowAmountMaximum = 0;
        borrowAPRMinimum = 0;
        borrowAPRMaximum = 0;
        borrowTermMinimum = 0;
        borrowTermMaximum = 0;
    }

    public AmortizationScheduleView(double borrowAmountMinimum, double borrowAmountMaximum,
                                    double borrowAPRMinimum, double borrowAPRMaximum,
                                    int borrowTermMinimum, int borrowTermMaximum)
    {
        this.borrowAmountMinimum = borrowAmountMinimum;
        this.borrowAmountMaximum = borrowAmountMaximum;
        this.borrowAPRMinimum = borrowAPRMinimum;
        this.borrowAPRMaximum = borrowAPRMaximum;
        this.borrowTermMinimum = borrowTermMinimum;
        this.borrowTermMaximum = borrowTermMaximum;
    }

    private static void printf(String formatString, Object... args) {

        try {
            if (console != null) {
                console.printf(formatString, args);
            } else {
                System.out.print(String.format(formatString, args));
            }
        } catch (IllegalFormatException e) {
            System.err.print("Error printing...\n");
        }
    }

    private static void print(String s) {
        printf("%s", s);
    }

    private static String readLine(String userPrompt) throws IOException {
        String line = "";

        if (console != null) {
            line = console.readLine(userPrompt);
        } else {
            // print("console is null\n");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            print(userPrompt);
            line = bufferedReader.readLine();
        }
        line.trim();
        return line;
    }

    // The output should include:
    //	The first column identifies the payment number.
    //	The second column contains the amount of the payment.
    //	The third column shows the amount paid to interest.
    //	The fourth column has the current balance.  The total payment amount and the interest paid fields.
    public void outputAmortizationSchedule(Payment[] payments) {
        String formatString = "%1$-20s%2$-20s%3$-20s%4$-20s%5$-20s%6$-20s\n";
        printf(formatString,
                "PaymentNumber", "PaymentAmount", "PaymentInterest",
                "CurrentBalance", "TotalPayments", "TotalInterestPaid");

        for (int index = 0; index < payments.length; index++)
        {
            // output is in dollars
            formatString = "%1$-20d%2$-20.2f%3$-20.2f%4$-20.2f%5$-20.2f%6$-20.2f\n";
            printf(formatString, payments[index].paymentNumber,
                    payments[index].paymentAmount,
                    payments[index].paymentInterest,
                    payments[index].currentBalance,
                    payments[index].totalPayments,
                    payments[index].totalInterestPaid);
        }
    }

    public void inputTerms()
    {
        String[] userPrompts = {
                "Please enter the amount you would like to borrow: ",
                "Please enter the annual percentage rate used to repay the loan: ",
                "Please enter the term, in years, over which the loan is repaid: "
        };

        String line = "";
        double amount = 0;
        double rate = 0;
        int years = 0;

        for (int i = 0; i< userPrompts.length; ) {
            String userPrompt = userPrompts[i];
            try {
                line = readLine(userPrompt);
            } catch (IOException e) {
                print("An IOException was encountered. Terminating program.\n");
                return;
            }

            boolean isValidValue = true;
            try {
                switch (i) {
                    case 0:
                        amount = Double.parseDouble(line);
                        if (amount < borrowAmountMinimum || amount > borrowAmountMaximum ) {
                            isValidValue = false;
                            print("Please enter a positive value between " + borrowAmountMinimum +
                                    " and " + borrowAmountMaximum + ". ");
                        }
                        break;
                    case 1:
                        rate = Double.parseDouble(line);
                        if (rate < borrowAPRMinimum || rate > borrowAPRMaximum) {
                            isValidValue = false;
                            print("Please enter a positive value between " + borrowAPRMinimum +
                                    " and " + borrowAPRMaximum + ". ");
                        }
                        break;
                    case 2:
                        years = Integer.parseInt(line);
                        if (years < borrowTermMinimum || years > borrowTermMaximum) {
                            isValidValue = false;
                            print("Please enter a positive integer value between " + borrowTermMinimum +
                                    " and " + borrowTermMaximum + ". ");
                        }
                        break;
                }
            } catch (NumberFormatException e) {
                isValidValue = false;
            }
            if (isValidValue) {
                i++;
            } else {
                print("An invalid value was entered.\n");
            }
        }

        setAmountBorrowed(new BigDecimal(amount * 100));
        setApr(new BigDecimal(rate));
        setInitialTermYears(new BigDecimal(years));
    }

    public BigDecimal getAmountBorrowed() {
        return amountBorrowed;
    }

    public void setAmountBorrowed(BigDecimal amountBorrowed) {
        this.amountBorrowed = amountBorrowed;
    }

    public BigDecimal getApr() {
        return apr;
    }

    public void setApr(BigDecimal apr) {
        this.apr = apr;
    }

    public BigDecimal getInitialTermYears() {
        return initialTermYears;
    }

    public void setInitialTermYears(BigDecimal initialTermYears) {
        this.initialTermYears = initialTermYears;
    }
}
