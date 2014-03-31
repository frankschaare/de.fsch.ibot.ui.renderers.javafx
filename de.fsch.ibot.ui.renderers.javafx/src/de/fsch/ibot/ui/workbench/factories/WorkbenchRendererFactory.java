package de.fsch.ibot.ui.workbench.factories;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;

import de.fsch.ibot.ui.internal.workbench.AbstractPartRenderer;
import de.fsch.ibot.ui.renderers.javafx.WBWRenderer;


public class WorkbenchRendererFactory implements IRendererFactory
{
private IEclipseContext context;	

private WBWRenderer wbwRenderer;

	public WorkbenchRendererFactory()
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public AbstractPartRenderer getRenderer(MUIElement uiElement, Object parent)
	{
		if (uiElement instanceof MWindow) 
		{
			if (wbwRenderer == null) 
			{
			wbwRenderer = new WBWRenderer();
			initRenderer(wbwRenderer);
			}
		return wbwRenderer;
		}

	return null;
	}
	
	protected void initRenderer(AbstractPartRenderer renderer) 
	{
	renderer.init(context);
	ContextInjectionFactory.inject(renderer, context);
	}
}
