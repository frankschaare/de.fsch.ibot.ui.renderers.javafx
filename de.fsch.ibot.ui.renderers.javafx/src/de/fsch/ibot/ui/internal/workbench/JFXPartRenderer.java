package de.fsch.ibot.ui.internal.workbench;

import java.util.List;

import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.MUILabel;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.IPresentationEngine;

public class JFXPartRenderer extends AbstractPartRenderer
{

	public JFXPartRenderer()
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object createControl(MUIElement element, Object parent)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void processContents(MElementContainer<MUIElement> container) 
	{
		// EMF gives us null lists if empty
		if (container == null)
			return;

	// Process any contents of the newly created ME
	List<MUIElement> parts = container.getChildren();
		
		if (parts != null) 
		{
		// loading a legacy app will add children to the window while it is being rendered.
		// this is *not* the correct place for this
		// hope that the ADD event will pick up the new part.
		IPresentationEngine renderer = (IPresentationEngine) context.get(IPresentationEngine.class.getName());
		MUIElement[] plist = parts.toArray(new MUIElement[parts.size()]);
			for (int i = 0; i < plist.length; i++) 
			{
			MUIElement childME = plist[i];
			renderer.createGui(childME);
			}
		}
	}

	@Override
	public void bindControl(MUIElement me, Object widget)
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected Object getParentWidget(MUIElement element)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void disposeControl(MUIElement part)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void hookControllerLogic(MUIElement me)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void childRendered(MElementContainer<MUIElement> parentElement,
			MUIElement element)
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected Object getImage(MUILabel element)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean requiresFocus(MPart element)
	{
		// TODO Auto-generated method stub
		return false;
	}

}
