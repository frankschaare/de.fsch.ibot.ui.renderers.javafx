package de.fsch.ibot.ui.renderers.javafx;

import javafx.scene.Group;
import javafx.scene.control.Control;

import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.services.IStylingEngine;

import de.fsch.ibot.ui.internal.workbench.JFXPartRenderer;

public class PerspectiveRenderer extends JFXPartRenderer
{

	public PerspectiveRenderer()
	{
	super();
	}
	
	public Group createControl(MUIElement element, Object parent) 
	{
		if (!(element instanceof MPerspective) || !(parent instanceof Control))
			return null;

	Group perspArea = new Group();
	// perspArea.setLayout(new FillLayout());
	IStylingEngine stylingEngine = (IStylingEngine) getContext(element).get(IStylingEngine.SERVICE_NAME);
	stylingEngine.setClassname(perspArea, "perspectiveLayout"); //$NON-NLS-1$

	return perspArea;
	}	

}
