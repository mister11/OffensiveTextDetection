package hr.fer.zemris.otd.liblinear;

import de.bwaldvogel.liblinear.Parameter;
import de.bwaldvogel.liblinear.SolverType;

/**
 * Created by Sven Vidak on 15.05.2014..
 */
public class Parameters {

	public static Parameter getInitParams() {
		Parameter params = new Parameter(SolverType.L2R_LR, 1, 0.001);
		return params;
	}
}
