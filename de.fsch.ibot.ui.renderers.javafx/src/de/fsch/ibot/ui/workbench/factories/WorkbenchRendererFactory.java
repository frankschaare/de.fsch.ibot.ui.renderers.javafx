package de.fsch.ibot.ui.workbench.factories;

import java.util.HashMap;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPlaceholder;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;

import de.fsch.ibot.ui.internal.workbench.AbstractPartRenderer;
import de.fsch.ibot.ui.renderers.javafx.WBWRenderer;

/**
 * The custom renderer factory is registered via the org.eclipse.core.runtime.products extension point. 
 * You use an additional property called rendererFactoryUri to point to the new factory class:
 * 
 * <property
 * 	name="rendererFactoryUri"
 *  value="bundleclass://de.fsch.ibot.ui.renderers.javafx/de.fsch.ibot.ui.workbench.factories.WorkbenchRendererFactory">
 * </property>
 *  
 * @author fsch
 * @since 01.04.2014	
 */
public class WorkbenchRendererFactory implements IRendererFactory
{
public static final String SHARED_ELEMENTS_STORE = "org.eclipse.e4.ui.workbench.renderers.swt.SHARED_ELEMENTS_STORE"; //$NON-NLS-1$	
private IEclipseContext context;	
private WBWRenderer wbwRenderer;

	public WorkbenchRendererFactory()
	{
		// TODO Auto-generated constructor stub
	}

	/*
	 * Hier wird der konkrete Renderer abgefragt. Wird aufgerufen von:
	 * - PartRenderingEngine#getRenderer(MUIElement uiElement, Object parent)
	 * 
	 * (non-Javadoc)
	 * @see de.fsch.ibot.ui.workbench.factories.IRendererFactory#getRenderer(org.eclipse.e4.ui.model.application.ui.MUIElement, java.lang.Object)
	 */
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
	
	@PostConstruct
	public void init(IEclipseContext context) 
	{
	this.context = context;
	this.context.set(SHARED_ELEMENTS_STORE,	new HashMap<MUIElement, Set<MPlaceholder>>());
	}
}
