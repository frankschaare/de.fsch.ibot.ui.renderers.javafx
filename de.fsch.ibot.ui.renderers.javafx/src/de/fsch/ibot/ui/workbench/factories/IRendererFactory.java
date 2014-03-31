package de.fsch.ibot.ui.workbench.factories;

import org.eclipse.e4.ui.model.application.ui.MUIElement;

import de.fsch.ibot.ui.internal.workbench.AbstractPartRenderer;

public interface IRendererFactory
{
public AbstractPartRenderer getRenderer(MUIElement uiElement, Object parent);
}
