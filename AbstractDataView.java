
import java.util.*;
import java.awt.*;
import javax.swing.JPanel;

public abstract class AbstractDataView extends JPanel 
implements Observer {
	private MapObjRegister register;

	public AbstractDataView(MapObjRegister observableRegister) 
	throws NullPointerException
	{
		if (observableRegister == null)
			throw new NullPointerException();
		register = observableRegister;
		register.addObserver(this);
	}

	public MapObjRegister getData(){
		return register;
	}

	protected abstract void updateDisplay();

	public void update(Observable observable, Object object){
		updateDisplay();
	}
}