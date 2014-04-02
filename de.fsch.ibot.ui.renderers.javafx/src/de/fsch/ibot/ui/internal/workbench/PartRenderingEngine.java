package de.fsch.ibot.ui.internal.workbench;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.contributions.IContributionFactory;
import org.eclipse.e4.ui.model.application.MApplicationElement;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.workbench.IPresentationEngine;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

import de.fsch.ibot.ui.workbench.factories.IRendererFactory;

public class PartRenderingEngine implements IPresentationEngine
{
@Inject EModelService modelService;	

private IRendererFactory curFactory = null;
private Map<String, AbstractPartRenderer> customRendererMap = new HashMap<String, AbstractPartRenderer>();
	

	public PartRenderingEngine()
	{
		// TODO Auto-generated constructor stub
	}
	
	/*
	 * Ist für das MUIElement ein Custom-Renderer definiert ?
	 * Wenn nicht, wird der Default-Renderer zurückgegeben
	 */
	private AbstractPartRenderer getRenderer(MUIElement uiElement, Object parent) 
	{
	// Is there a custom renderer defined ?
	String customURI = uiElement.getPersistedState().get(IPresentationEngine.CUSTOM_RENDERER_KEY);
		if (customURI != null) 
		{
			if (customRendererMap.get(customURI) instanceof AbstractPartRenderer)
			{
			return customRendererMap.get(customURI);
			}

		IEclipseContext owningContext = modelService.getContainingContext(uiElement);
		IContributionFactory contributionFactory = (IContributionFactory) owningContext.get(IContributionFactory.class.getName());
		
		Object customRenderer = contributionFactory.create(customURI, owningContext);
			if (customRenderer instanceof AbstractPartRenderer) 
			{
			customRendererMap.put(customURI, (AbstractPartRenderer) customRenderer);
			return (AbstractPartRenderer) customRenderer;
			}
		}

		// If not then use the default renderer
	return curFactory.getRenderer(uiElement, parent);
	}

	protected AbstractPartRenderer getRendererFor(MUIElement element) {return (AbstractPartRenderer) element.getRenderer();}
	

	@Override
	public Object createGui(MUIElement element, Object parentWidget,
			IEclipseContext parentContext)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object createGui(MUIElement element)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeGui(MUIElement element)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void focusGui(MUIElement element)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Object run(MApplicationElement uiRoot, IEclipseContext appContext)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void stop()
	{
		// TODO Auto-generated method stub

	}

}
