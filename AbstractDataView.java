import java.util.*;
import java.awt.*;
import javax.swing.JPanel;

public abstract class AbstractDataView extends JPanel 
implements Observer {
	private Data data;

	public AbstractDataView(Data observableData) 
	throws NullPointerException
	{
		if (observableData == null)
			throw new NullPointerException();
		data = observableData;
		data.addObserver(this);
	}

	public Data getData(){
		return data;
	}

	protected abstract void updateDisplay();

	public void update(Observable observable, Object object){
		updateDisplay();
	}
}