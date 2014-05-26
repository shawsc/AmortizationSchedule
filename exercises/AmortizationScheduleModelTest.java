package exercises;

/**
 * Created by Steven on 5/26/2014.
 */
import java.math.BigDecimal;

class AmortizationScheduleModelTest {
    void testGenerateAmortizationScheduleHighBorrow() {
        System.out.print("Test an out of bounds (high) amount to borrow.");
        try {
            AmortizationScheduleModel theModel = new AmortizationScheduleModel();
            Payment[] payments = theModel.GenerateAmortizationSchedule(new BigDecimal("1000000000001"),
                new BigDecimal("5"), new BigDecimal("30"));
        }
        catch (IllegalArgumentException e){
            assert true;
            System.out.print("   Success!\n");
            return;
        }
        System.out.print("   Fail!\n");
        assert false;
    }
    void testGenerateAmortizationScheduleLowBorrow() {
        System.out.print("Test an out of bounds (low) amount to borrow.");
        try {
            AmortizationScheduleModel theModel = new AmortizationScheduleModel();
            Payment[] payments = theModel.GenerateAmortizationSchedule(new BigDecimal("0"),
                    new BigDecimal("5"), new BigDecimal("30"));
        }
        catch (IllegalArgumentException e){
            assert true;
            System.out.print("   Success!\n");
            return;
        }
        System.out.print("   Fail!\n");
        assert false;
    }
    void testGenerateAmortizationScheduleHighAPR() {
        System.out.print("Test an out of bounds (high) APR.");
        try {
            AmortizationScheduleModel theModel = new AmortizationScheduleModel();
            Payment[] payments = theModel.GenerateAmortizationSchedule(new BigDecimal("100000"),
                    new BigDecimal("101"), new BigDecimal("30"));
        }
        catch (IllegalArgumentException e){
            assert true;
            System.out.print("   Success!\n");
            return;
        }
        System.out.print("   Fail!\n");
        assert false;
    }
    void testGenerateAmortizationScheduleLowAPR() {
        System.out.print("Test an out of bounds (low) APR.");
        try {
            AmortizationScheduleModel theModel = new AmortizationScheduleModel();
            Payment[] payments = theModel.GenerateAmortizationSchedule(new BigDecimal("100000"),
                    new BigDecimal("0.0000001"), new BigDecimal("30"));
        }
        catch (IllegalArgumentException e){
            assert true;
            System.out.print("   Success!\n");
            return;
        }
        System.out.print("   Fail!\n");
        assert false;
    }
    void testGenerateAmortizationScheduleHighTerm() {
        System.out.print("Test an out of bounds (high) term.");
        try {
            AmortizationScheduleModel theModel = new AmortizationScheduleModel();
            Payment[] payments = theModel.GenerateAmortizationSchedule(new BigDecimal("100000"),
                    new BigDecimal("5"), new BigDecimal("1000001"));
        }
        catch (IllegalArgumentException e){
            assert true;
            System.out.print("   Success!\n");
            return;
        }
        System.out.print("   Fail!\n");
        assert false;
    }
    void testGenerateAmortizationScheduleLowTerm() {
        System.out.print("Test an out of bounds (low) term.");
        try {
            AmortizationScheduleModel theModel = new AmortizationScheduleModel();
            Payment[] payments = theModel.GenerateAmortizationSchedule(new BigDecimal("100000"),
                    new BigDecimal("5"), new BigDecimal("0"));
        }
        catch (IllegalArgumentException e){
            assert true;
            System.out.print("   Success!\n");
            return;
        }
        System.out.print("   Fail!\n");
        assert false;
    }
    void testGenerateAmortizationSchedule() {
        System.out.print("Test that when borrowing for 30 years the payments array contains 360 payments.");
        Payment[] payments;
        try {
            AmortizationScheduleModel theModel = new AmortizationScheduleModel();
            payments = theModel.GenerateAmortizationSchedule(new BigDecimal("100000"),
                    new BigDecimal("5"), new BigDecimal("30"));
        }
        catch (IllegalArgumentException e){
            assert false;
            System.out.print("   Fail!\n");
            return;
        }
        // The first element is not a payment
        if (payments.length == 361 && payments[360].currentBalance == 0.0) {
            assert true;
            System.out.print("   Success!\n");
        }
        else {
            assert false;
            System.out.print("   Fail!\n");
        }
    }
    void testCalculateMonthlyPayment() {
        System.out.print("Test that the correct monthly payment is calculated.");

        AmortizationScheduleModel theModel = new AmortizationScheduleModel();
        theModel.setAmountBorrowed(new BigDecimal("100000"));
        theModel.setApr(new BigDecimal("5"));
        theModel.setInitialTermMonths(new BigDecimal("360"));
        if (theModel.calculateMonthlyPayment().setScale(2,BigDecimal.ROUND_HALF_UP).compareTo(new BigDecimal("536.85")) == 0) {
            assert true;
            System.out.print("   Success!\n");
        }
        else {
            assert false;
            System.out.print("   Fail!\n");
        }
    }
}
