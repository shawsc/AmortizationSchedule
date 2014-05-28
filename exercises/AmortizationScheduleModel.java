package exercises;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AmortizationScheduleModel {
    protected BigDecimal amountBorrowed;        // in cents
    protected BigDecimal apr;
    protected BigDecimal initialTermMonths;

    protected BigDecimal monthlyInterest;
    protected BigDecimal monthlyPaymentAmount;    // in cents

    protected static final BigDecimal[] borrowAmountRange;
    protected static final BigDecimal[] aprRange;
    protected static final BigDecimal[] termRange;

    static {
        borrowAmountRange = new BigDecimal[]{new BigDecimal(0.01d), new BigDecimal(1000000000000d)};
        aprRange = new BigDecimal[]{new BigDecimal(0.000001d), new BigDecimal(100d)};
        termRange = new BigDecimal[]{BigDecimal.ONE, new BigDecimal(1000000)};
    }

    {
        amountBorrowed = BigDecimal.ZERO;
        apr = BigDecimal.ZERO;
        initialTermMonths = BigDecimal.ZERO;
        monthlyInterest = BigDecimal.ZERO;
        monthlyPaymentAmount = BigDecimal.ZERO;
    }

    public AmortizationScheduleModel()
    {

    }

    public Payment[] GenerateAmortizationSchedule(BigDecimal amount, BigDecimal interestRate, BigDecimal years) throws IllegalArgumentException {

        if ((isValidBorrowAmount(amount) == false) ||
                (isValidAPRValue(interestRate) == false) ||
                (isValidTerm(years) == false)) {
            throw new IllegalArgumentException();
        }

        setAmountBorrowed(amount);
        setApr(interestRate);
        setInitialTermMonths(years.multiply(new BigDecimal(12)));

        setMonthlyPaymentAmount(calculateMonthlyPayment());

        // the following shouldn't happen with the available valid ranges
        // for borrow amount, apr, and term; however, without range validation,
        // monthlyPaymentAmount as calculated by calculateMonthlyPayment()
        // may yield incorrect values with extreme input values
        if (monthlyPaymentAmount.compareTo(amountBorrowed) > 0) {
            throw new IllegalArgumentException();
        }

        //
        // To create the amortization table, create a loop in your program and follow these steps:
        // 1.      Calculate H = P x J, this is your current monthly interest
        // 2.      Calculate C = M - H, this is your monthly payment minus your monthly interest, so it is the amount of principal you pay for that month
        // 3.      Calculate Q = P - C, this is the new balance of your principal of your loan.
        // 4.      Set P equal to Q and go back to Step 1: You thusly loop around until the value Q (and hence P) goes to zero.
        //

        BigDecimal balance = amountBorrowed;
        int paymentNumber = 0;
        BigDecimal totalPayments = BigDecimal.ZERO;
        BigDecimal totalInterestPaid = BigDecimal.ZERO;

        final int maxNumberOfPayments = initialTermMonths.intValue() + 1;
        Payment[] payments = new Payment[maxNumberOfPayments];
        payments[paymentNumber] = new Payment(0,0d,0d,amountBorrowed.doubleValue() / 100d,0d,0d);

        paymentNumber++;

        while (balance.compareTo(BigDecimal.ZERO) > 0 && paymentNumber < maxNumberOfPayments) {
            // Calculate H = P x J, this is your current monthly interest
            BigDecimal curMonthlyInterest = balance.multiply(monthlyInterest);

            // the amount required to payoff the loan
            BigDecimal curPayoffAmount = balance.add(curMonthlyInterest);

            // the amount to payoff the remaining balance may be less than the calculated monthlyPaymentAmount
            BigDecimal curMonthlyPaymentAmount = curPayoffAmount.min(monthlyPaymentAmount);

            // it's possible that the calculated monthlyPaymentAmount is 0,
            // or the monthly payment only covers the interest payment - i.e. no principal
            // so the last payment needs to payoff the loan
            if ((paymentNumber == maxNumberOfPayments) &&
                    (curMonthlyPaymentAmount.equals(BigDecimal.ZERO) ||
                            (curMonthlyPaymentAmount.equals(curMonthlyInterest)))) {
                curMonthlyPaymentAmount = curPayoffAmount;
            }

            // Calculate C = M - H, this is your monthly payment minus your monthly interest,
            // so it is the amount of principal you pay for that month
            BigDecimal curMonthlyPrincipalPaid = curMonthlyPaymentAmount.subtract(curMonthlyInterest);

            // Calculate Q = P - C, this is the new balance of your principal of your loan.
            BigDecimal curBalance = balance.subtract(curMonthlyPrincipalPaid);

            totalPayments = totalPayments.add(curMonthlyPaymentAmount);
            totalInterestPaid = totalInterestPaid.add(curMonthlyInterest);

            // output is in dollars

            payments[paymentNumber] = new Payment(paymentNumber,
                    (curMonthlyPaymentAmount.doubleValue()) / 100d,
                    (curMonthlyInterest.doubleValue()) / 100d,
                    (curBalance.doubleValue()) / 100d,
                    (totalPayments.doubleValue()) / 100d,
                    (totalInterestPaid.doubleValue()) / 100d);

            // Set P equal to Q and go back to Step 1: You thusly loop around until the value Q (and hence P) goes to zero.
            balance = curBalance;

            paymentNumber++;
        }

        // Check for rounding error!
        assert (paymentNumber > maxNumberOfPayments);

        return payments;
    }

    public static boolean isValidBorrowAmount(BigDecimal amount) {
        BigDecimal range[] = getBorrowAmountRange();
        return ((range[0].compareTo(amount)<1) && (amount.compareTo(range[1])<1));
    }

    public static boolean isValidAPRValue(BigDecimal rate) {
        BigDecimal range[] = getAPRRange();
        return ((range[0].compareTo(rate)<1) && (rate.compareTo(range[1])<1));
    }

    public static boolean isValidTerm(BigDecimal years) {
        BigDecimal range[] = getTermRange();
        return ((range[0].compareTo(years)<1) && (years.compareTo(range[1])<1));
    }

    public static final BigDecimal[] getBorrowAmountRange() {
        return borrowAmountRange;
    }

    public static final BigDecimal[] getAPRRange() {
        return aprRange;
    }

    public static final BigDecimal[] getTermRange() {
        return termRange;
    }

    protected BigDecimal calculateMonthlyPayment() {
        // M = P * (J / (1 - (Math.pow(1/(1 + J), N))));
        //
        // Where:
        // P = Principal
        // I = Interest
        // J = Monthly Interest in decimal form:  I / (12 * 100)
        // N = Number of months of loan
        // M = Monthly Payment Amount
        //

        // calculate J
        monthlyInterest = apr.divide((new BigDecimal("1200")), 10, RoundingMode.HALF_UP);

        // this is 1 / (1 + J)
        BigDecimal tmp = monthlyInterest.add(BigDecimal.ONE);
        tmp = BigDecimal.ONE.divide(tmp, 10, RoundingMode.HALF_UP);

        // this is Math.pow(1/(1 + J), N)
        tmp = tmp.pow(initialTermMonths.intValue());

        // this is J / (1 - (Math.pow(1/(1 + J), N))))
        tmp = BigDecimal.ONE.subtract(tmp);
        tmp = monthlyInterest.divide(tmp,10,RoundingMode.HALF_UP);

        // M = P * (J / (1 - (Math.pow(1/(1 + J), N))));
        BigDecimal rc = amountBorrowed.multiply(tmp);
        return rc;
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

    public BigDecimal getInitialTermMonths() {
        return initialTermMonths;
    }

    public void setInitialTermMonths(BigDecimal initialTermMonths) {
        this.initialTermMonths = initialTermMonths;
    }

    public BigDecimal getMonthlyInterest() {
        return monthlyInterest;
    }

    public void setMonthlyInterest(BigDecimal monthlyInterest) {
        this.monthlyInterest = monthlyInterest;
    }

    public BigDecimal getMonthlyPaymentAmount() {
        return monthlyPaymentAmount;
    }

    public void setMonthlyPaymentAmount(BigDecimal monthlyPaymentAmount) {
        this.monthlyPaymentAmount = monthlyPaymentAmount;
    }
}
