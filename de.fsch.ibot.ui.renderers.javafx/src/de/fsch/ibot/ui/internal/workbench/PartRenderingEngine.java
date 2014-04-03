package de.fsch.ibot.ui.internal.workbench;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.contributions.IContributionFactory;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.model.application.MApplicationElement;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.workbench.IPresentationEngine;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

import de.fsch.ibot.ui.workbench.factories.IRendererFactory;

public class PartRenderingEngine implements IPresentationEngine
{
@Inject EModelService modelService;	
@Inject protected Logger logger;
@Inject @Optional protected IEventBroker eventBroker;
private IEclipseContext appContext;
private IRendererFactory curFactory = null;
private Map<String, AbstractPartRenderer> customRendererMap = new HashMap<String, AbstractPartRenderer>();

private static final String DEFAULT_WORRKBENCHRENDERERFACTORY = "bundleclass://de.fsch.ibot.ui.renderers.javafx/de.fsch.ibot.ui.workbench.factories.WorkbenchRendererFactory";
public static final String PARTRENDERINGENGINE_URI = "bundleclass://de.fsch.ibot.ui.renderers.javafx/de.fsch.ibot.ui.internal.workbench.PartRenderingEngine";
private String factoryUrl;
	

	public PartRenderingEngine()
	{
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Initialize a part renderer from the extension point.
	 * 
	 * @param context the context for the part factories
	 */
	@PostConstruct
	void initialize(IEclipseContext context) 
	{
	this.appContext = context;

	/* initialize the correct key-binding display formatter
		KeyFormatterFactory.setDefault(SWTKeySupport.getKeyFormatterForPlatform());
	*/
		// Add the renderer to the context
	context.set(IPresentationEngine.class.getName(), this);

	IRendererFactory factory = null;
	IContributionFactory contribFactory = context.get(IContributionFactory.class);
		try 
		{
		factory = (IRendererFactory) contribFactory.create(factoryUrl,	context);
		} 
		catch (Exception e) 
		{
		logger.warn(e, "Could not create rendering factory");
		}

		// Try to load the default one
		if (factory == null) 
		{
			try 
			{
			factory = (IRendererFactory) contribFactory.create(PartRenderingEngine.DEFAULT_WORRKBENCHRENDERERFACTORY, context);
			} 
			catch (Exception e) 
			{
			logger.error(e, "Could not create default rendering factory");
			}
		}

		if (factory == null) 
		{
		throw new IllegalStateException("Could not create any rendering factory. Aborting ...");
		}

	curFactory = factory;
	context.set(IRendererFactory.class, curFactory);

		// Hook up the widget life-cycle subscriber
		if (eventBroker != null) 
		{
		// TODO eventBroker.subscribe(UIEvents.UIElement.TOPIC_TOBERENDERED, toBeRenderedHandler);
		// TODO eventBroker.subscribe(UIEvents.UIElement.TOPIC_VISIBLE, visibilityHandler);
		// TODO eventBroker.subscribe(UIEvents.ElementContainer.TOPIC_CHILDREN, childrenHandler);
		// TODO NeventBroker.subscribe(UIEvents.Window.TOPIC_WINDOWS, windowsHandler);
		// TODO eventBroker.subscribe(UIEvents.Perspective.TOPIC_WINDOWS, windowsHandler);
		// TODO eventBroker.subscribe(UIEvents.TrimmedWindow.TOPIC_TRIMBARS, trimHandler);
		}
	}	
	
	public Object safeCreateGui(MUIElement element, Object parentWidget, IEclipseContext parentContext) 
	{
	return null;	
	}
	
	/*
	 * Hier wird der Renderer im UI-Objekt gespeichert
	 * 
	 * Wird aufgerufen von safeCreateGui(MUIElement element, Object parentWidget, IEclipseContext parentContext)
	 */
	protected Object createWidget(MUIElement element, Object parent) 
	{
	AbstractPartRenderer renderer = getRenderer(element, parent);
		if (renderer != null) 
		{
		// Remember which renderer is responsible for this widget
		element.setRenderer(renderer);
		Object newWidget = renderer.createWidget(element, parent);
			if (newWidget != null) 
			{
			renderer.bindWidget(element, newWidget);
			return newWidget;
			}
		}

		return null;
	}
	
	/*
	 * Ist für das MUIElement ein Custom-Renderer definiert ?
	 * Wenn nicht, wird der Default-Renderer zurückgegeben
	 * 
	 * Wird aufgerufen von createWidget(MUIElement element, Object parent)
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
