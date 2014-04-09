package de.fsch.ibot.ui.internal.workbench;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.contributions.IContributionFactory;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MApplicationElement;
import org.eclipse.e4.ui.model.application.ui.MContext;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPlaceholder;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.IPresentationEngine;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.equinox.app.IApplication;

import de.fsch.ibot.ui.renderers.javafx.FXApplication;
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
private FXApplication fxApp = null;
protected MApplication e4App;
	

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
	
	
	/*
	 * Wird aufgerufen von safeCreateGui(MUIElement element)
	 */
	public Object safeCreateGui(MUIElement element, Object parentWidget, IEclipseContext parentContext) 
	{
		if (!element.isToBeRendered()) return null;

		//TODO  no creates while processing a remove
		/*
		if (removeRoot != null) 
		{
			return null;
		}
		 */

	Object currentWidget = element.getWidget();
		if (currentWidget != null) 
		{
			// TODO SWT Shit ersetzen
			/*
			if (currentWidget instanceof Control) 
			{
				Control control = (Control) currentWidget;
				// make sure the control is visible
				if (!(element instanceof MPlaceholder))
					control.setVisible(true);

				if (parentWidget instanceof Composite) {
					Composite currentParent = control.getParent();
					if (currentParent != parentWidget) {
						// check if the original parent was a tab folder
						if (currentParent instanceof CTabFolder) {
							CTabFolder folder = (CTabFolder) currentParent;
							// if we used to be the tab folder's top right
							// control, unset it
							if (folder.getTopRight() == control) {
								folder.setTopRight(null);
							}
						}

						// the parents are different so we should reparent it
						control.setParent((Composite) parentWidget);
					}
				}
			}
			 */

			// Reparent the context (or the kid's context)
			if (element instanceof MContext) 
			{
			IEclipseContext ctxt = ((MContext) element).getContext();
				if (ctxt != null) ctxt.setParent(parentContext);
			} 
			else 
			{
			List<MContext> childContexts = modelService.findElements(element, null, MContext.class, null);
				for (MContext c : childContexts) 
				{
				// Ensure that we only reset the context of our direct children
				MUIElement kid = (MUIElement) c;
				MUIElement parent = kid.getParent();
					if (parent == null && kid.getCurSharedRef() != null)
						parent = kid.getCurSharedRef().getParent();
					if (!(element instanceof MPlaceholder) && parent != element)
						continue;
					if (c.getContext() != null && c.getContext().getParent() != parentContext) 
					{
					c.getContext().setParent(parentContext);
					}
				}
			}

			// Now that we have a widget let the parent (if any) know
			if (element.getParent() instanceof MUIElement) 
			{
			MElementContainer<MUIElement> parentElement = element.getParent();
			AbstractPartRenderer parentRenderer = getRendererFor(parentElement);
				if (parentRenderer != null)
				{
				parentRenderer.childRendered(parentElement, element);
				}
			}
		return element.getWidget();
		}

		if (element instanceof MContext) 
		{
		MContext ctxt = (MContext) element;
			// Assert.isTrue(ctxt.getContext() == null,
			// "Before rendering Context should be null");
			if (ctxt.getContext() == null) 
			{
			IEclipseContext lclContext = parentContext.createChild(getContextName(element));
			populateModelInterfaces(ctxt, lclContext, element.getClass().getInterfaces());
			ctxt.setContext(lclContext);

			// System.out.println("New Context: " + lclContext.toString() + " parent: " + parentContext.toString());

			// make sure the context knows about these variables that have been defined in the model
				for (String variable : ctxt.getVariables()) 
				{
				lclContext.declareModifiable(variable);
				}

			Map<String, String> props = ctxt.getProperties();
				for (String key : props.keySet()) 
				{
				lclContext.set(key, props.get(key));
				}
			}
		}

		// Create a control appropriate to the part
		Object newWidget = createWidget(element, parentWidget);

		// Remember that we've created the control
			if (newWidget != null) 
			{
			AbstractPartRenderer renderer = getRendererFor(element);

			// Have the renderer hook up any widget specific listeners
			renderer.hookControllerLogic(element);

				// Process its internal structure through the renderer that created
				// it
				if (element instanceof MElementContainer) 
				{
				renderer.processContents((MElementContainer<MUIElement>) element);
				}

			// Allow a final chance to set up
			renderer.postProcess(element);

			// Now that we have a widget let the parent (if any) know
				if (element.getParent() instanceof MUIElement) 
				{
				MElementContainer<MUIElement> parentElement = element.getParent();
				AbstractPartRenderer parentRenderer = getRendererFor(parentElement);
					if (parentRenderer != null) parentRenderer.childRendered(parentElement, element);
				}
			} 
			else 
			{
				// failed to create the widget, dispose its context if necessary
				if (element instanceof MContext) 
				{
				MContext ctxt = (MContext) element;
				IEclipseContext lclContext = ctxt.getContext();
					if (lclContext != null) 
					{
					lclContext.dispose();
					ctxt.setContext(null);
					}
				}
		}
	return newWidget;
	}
	
	private static void populateModelInterfaces(MContext contextModel, IEclipseContext context, Class<?>[] interfaces) 
	{
		for (Class<?> intf : interfaces) 
		{
		// TODO Activator.trace(Policy.DEBUG_CONTEXTS, "Adding " + intf.getName() + " for " + contextModel.getClass().getName(), null);
		context.set(intf.getName(), contextModel);

		populateModelInterfaces(contextModel, context, intf.getInterfaces());
		}
	}	
	
	private String getContextName(MUIElement element) 
	{
	StringBuilder builder = new StringBuilder(element.getClass().getSimpleName());
	String elementId = element.getElementId();
		if (elementId != null && elementId.length() != 0) 
		{
		builder.append(" (").append(elementId).append(") ");
		}
	builder.append("Context");
	
	return builder.toString();
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
		Object newControl = renderer.createControl(element, parent);
			if (newControl != null) 
			{
			renderer.bindControl(element, newControl);
			return newControl;
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

	// Wichtig: hier wird die WorkbenchRendererFactory aufgerufen und es wird der konkrete Renderer abgefragt
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
	public Object createGui(final MUIElement element)
	{
	final Object[] gui = { null };
	
	// wrap the handling in a SafeRunner so that exceptions do not prevent
	// the renderer from processing other elements
	/*
	SafeRunner.run(new ISafeRunnable() 
		{
		public void handleException(Throwable e) 
		{
			if (e instanceof Error) 
			{
			// errors are deadly, we shouldn't ignore these
			throw (Error) e;
			} 
			else 
			{
				// log exceptions otherwise
				if (logger != null) 
				{
				String message = "Exception occurred while rendering: {0}"; 
				logger.error(e, message);
				}
			}
		}

		public void run() throws Exception 
		{
			System.out.println(element.getElementId());
		gui[0] = safeCreateGui(element);
		}
		});
		*/
	gui[0] = safeCreateGui(element);
	return gui[0];
	}

	/*
	 * Wird aufgerufen von createGui(MUIElement element)
	 */
	private Object safeCreateGui(MUIElement element) 
	{
	// Obtain the necessary parent widget
	Object parent = null;
	MUIElement parentME = element.getParent();
		if (parentME == null)
		{
		parentME = (MUIElement) ((EObject) element).eContainer();
		}
		if (parentME != null) 
		{
		AbstractPartRenderer renderer = getRendererFor(parentME);
			if (renderer != null) 
			{
				if (!element.isVisible()) 
				{
				// TODO parent = getLimboShell();
				} 
				else 
				{
				parent = renderer.getUIContainer(element);
				}
			}
		}

	// Obtain the necessary parent context
	IEclipseContext parentContext = null;
		if (element.getCurSharedRef() != null) 
		{
		MPlaceholder ph = element.getCurSharedRef();
		parentContext = getContext(ph.getParent());
		} 
		else if (parentContext == null && element.getParent() != null) 
		{
		parentContext = getContext(element.getParent());
		} 
		else if (parentContext == null && element.getParent() == null) 
		{
		parentContext = getContext((MUIElement) ((EObject) element).eContainer());
		}

	return safeCreateGui(element, parent, parentContext);
	}	
	
	private IEclipseContext getContext(MUIElement parent) 
	{
		if (parent instanceof MContext) 
		{
		return ((MContext) parent).getContext();
		}
	return modelService.getContainingContext(parent);
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

	/*
	 * Hier wird die GUI gestartet.
	 * Wird aufgerufen von E4Workbench#createAndRunUI(MApplicationElement uiRoot)
	 * (non-Javadoc)
	 * @see org.eclipse.e4.ui.workbench.IPresentationEngine#run(org.eclipse.e4.ui.model.application.MApplicationElement, org.eclipse.e4.core.contexts.IEclipseContext)
	 */
	public Object run(final MApplicationElement uiRoot, final IEclipseContext runContext) 
	{
	/*	
	 * Wichtig: 
	 * Ich habe im SWT-Renderer nicht gefunden, wo die RenderFactory initialisiert wird,
	 * daher initialisiere ich erst einmal hier.
	 * Möglicherweise muss das nochmal geändert werden.
	 */
	initialize(runContext);
	
	// UI Thread starten
	fxApp = new FXApplication();
	e4App = (MApplication) uiRoot;
	
	// long startTime = System.currentTimeMillis();
	MWindow selected = e4App.getSelectedElement();
	
		if (selected == null) 
		{
			for (MWindow window : e4App.getChildren()) 
			{
			createGui(window);
			}
		} 
		else 
		{
		// render the selected one first
		createGui(selected);
			for (MWindow window : e4App.getChildren()) 
			{
				if (selected != window) 
				{
				createGui(window);
				}
		}
	}
	fxApp.launch(fxApp.getClass());

	return IApplication.EXIT_OK;
	}

	@Override
	public void stop()
	{
		// TODO Auto-generated method stub

	}

}
