package exercises;

/**
 * Created by Steven on 5/25/2014.
 */


public class AmortizationScheduleController {

    protected AmortizationScheduleModel theModel;
    protected AmortizationScheduleView theView;

    public AmortizationScheduleController(AmortizationScheduleModel model, AmortizationScheduleView view)
    {
        theModel = model;
        theView = view;
    }

    public void DoAmortizationSchedule()
    {
        theView.inputTerms();

        Payment[] payments = theModel.GenerateAmortizationSchedule(theView.getAmountBorrowed(),
                theView.getApr(),
                theView.getInitialTermYears());

        theView.outputAmortizationSchedule(payments);
    }
}
