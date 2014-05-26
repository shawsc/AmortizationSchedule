package exercises;

/**
 * Created by Steven on 5/25/2014.
 */

public class Payment {
    public int paymentNumber = 0;
    public double paymentAmount = 0d;
    public double paymentInterest = 0d;
    public double currentBalance = 0d;
    public double totalPayments = 0d;
    public double totalInterestPaid = 0d;

    public Payment(int paymentNumber,double paymentAmount,double paymentInterest,
                   double currentBalance, double totalPayments, double totalInterestPaid){
        this.paymentNumber = paymentNumber;
        this.paymentAmount = paymentAmount;
        this.paymentInterest = paymentInterest;
        this.currentBalance = currentBalance;
        this.totalPayments = totalPayments;
        this.totalInterestPaid = totalInterestPaid;
    }
}
