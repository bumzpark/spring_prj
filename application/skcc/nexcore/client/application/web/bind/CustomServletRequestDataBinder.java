package skcc.nexcore.client.application.web.bind;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyBatchUpdateException;
import org.springframework.beans.PropertyValue;
import org.springframework.web.bind.ServletRequestDataBinder;

import skcc.nexcore.client.application.web.HttpParameterData;

public class CustomServletRequestDataBinder extends ServletRequestDataBinder {

	public CustomServletRequestDataBinder(Object target) {
		super(target);
	}

	public CustomServletRequestDataBinder(Object target, String objectName) {
		super(target, objectName);
	}

	protected void applyPropertyValues(MutablePropertyValues mpvs) {
		try {
			PropertyValue[] values = mpvs.getPropertyValues();
			if (values != null) {
				HttpParameterData param = (HttpParameterData) getTarget();
				for (PropertyValue pv : values) {
					param.add(pv.getName(), pv.getValue());
				}
			}
		} catch (PropertyBatchUpdateException ex) {
			org.springframework.beans.PropertyAccessException exs[] = ex.getPropertyAccessExceptions();
			for (int i = 0; i < exs.length; i++)
				getBindingErrorProcessor().processPropertyAccessException(exs[i], getInternalBindingResult());
		}
	}
}
