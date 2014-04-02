package de.fsch.ibot.ui.renderers.javafx;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.sun.javafx.geom.Rectangle;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage; 

public class WBWRenderer extends JavaFXPartRenderer
{
private static String ShellMinimizedTag = "shellMinimized"; //$NON-NLS-1$
private static String ShellMaximizedTag = "shellMaximized"; //$NON-NLS-1$

	public WBWRenderer()
	{
	super();
	}
	
	/*
	 * Hier wird die SWT-Shell initialisiert.
	 * Ein JavaFX Renderer muss an dieser Stelle die Primary Stage initialisieren
	 *  
	 */
	public Object createWidget(MUIElement element, Object parent) 
	{
	final Stage wbwStage;

		// TODO: Die Prüfung: && !(parent instanceof Control) habe ich rausgenommen
		if (!(element instanceof MWindow) || (parent != null))
		{
		return null;	
		}

	MWindow wbwModel = (MWindow) element;

	MApplication appModel = wbwModel.getContext().get(MApplication.class);
	
	/*
	 * Verstehe ich noch nicht
	 * RTL = Rigth to Left style
	Boolean rtlMode = (Boolean) appModel.getTransientData().get(E4Workbench.RTL_MODE);
	int rtlStyle = (rtlMode != null && rtlMode.booleanValue()) ? SWT.RIGHT_TO_LEFT : 0;
	 * 
	 */

	//	TODO: ((Control) parent).getShell() für JavaFX umsetzen
	//	Shell parentShell = parent == null ? null : ((Control) parent).getShell();
	Stage parentStage = parent == null ? null : null;

		// Top-Level
		if (parentStage == null) 
		{
		wbwStage = new Stage();
		wbwModel.getTags().add("topLevel");
		}
		else if (wbwModel.getTags().contains("dragHost")) 
		{
		// TODO: Drag'nDrop ?
		// wbwShell = new Shell(parentShell, SWT.BORDER | rtlStyle);
		// wbwShell.setAlpha(110);
		wbwStage = new Stage();	
		} 
		// Window, Wizard, Dialog
		else 
		{
		wbwStage = new Stage();	
		// wbwShell = new Shell(parentShell, SWT.TITLE | SWT.RESIZE | SWT.MAX	| SWT.CLOSE | rtlStyle);

			// TODO: Was genau macht der TaverseListener ?
			/* Prevent ESC from closing the DW
			wbwShell.addTraverseListener(new TraverseListener() 
			{
				public void keyTraversed(TraverseEvent e) {
					if (e.detail == SWT.TRAVERSE_ESCAPE) {
						e.doit = false;
					}
				}
			});
			*/
		}

		// TODO: Hintergrundmodus definieren
		// wbwShell.setBackgroundMode(SWT.INHERIT_DEFAULT);

	Rectangle modelBounds = new Rectangle();
		if (wbwModel instanceof EObject) 
		{
		EObject wbw = (EObject) wbwModel;
		EClass wbwclass = wbw.eClass();
			
			// Fensterangaben aus dem Applikationsmodel laden:
			if (wbw.eIsSet(wbwclass.getEStructuralFeature("x"))) 
			{
			modelBounds.x = wbwModel.getX();
			}
			if (wbw.eIsSet(wbwclass.getEStructuralFeature("y"))) 
			{
			modelBounds.y = wbwModel.getY();
			}
			if (wbw.eIsSet(wbwclass.getEStructuralFeature("height"))) 
			{
			modelBounds.height = wbwModel.getHeight();
			}
			if (wbw.eIsSet(wbwclass.getEStructuralFeature("width"))) 
			{
			modelBounds.width = wbwModel.getWidth();
			}
		}
	// Force the shell onto the display if it would be invisible otherwise
	Rectangle2D displayBounds = Screen.getPrimary().getVisualBounds();
	Rectangle2D modelBounds2D = new Rectangle2D(modelBounds.x, modelBounds.y, modelBounds.height, modelBounds.width);
	
		if (! modelBounds2D.intersects(displayBounds)) 
		{
		// Rectangle clientArea = Display.getCurrent().getClientArea();
		// modelBounds.x = clientArea.x;
		// modelBounds.y = clientArea.y;
		}
		wbwStage.setHeight(modelBounds.x);
		wbwStage.setHeight(modelBounds.y);
		wbwStage.centerOnScreen();

		// TODO: 
		// setCSSInfo(wbwModel, wbwShell);

		// set up context
		IEclipseContext localContext = getContext(wbwModel);
		/*

		// We need to retrieve specific CSS properties for our layout.
		CSSEngineHelper helper = new CSSEngineHelper(localContext, wbwShell);
		TrimmedPartLayout tl = new TrimmedPartLayout(wbwShell);
		tl.gutterTop = helper.getMarginTop(0);
		tl.gutterBottom = helper.getMarginBottom(0);
		tl.gutterLeft = helper.getMarginLeft(0);
		tl.gutterRight = helper.getMarginRight(0);

		wbwShell.setLayout(tl);
		newWidget = wbwShell;
		bindWidget(element, newWidget);

		// Add the shell into the WBW's context
		localContext.set(Shell.class.getName(), wbwShell);
		localContext.set(E4Workbench.LOCAL_ACTIVE_SHELL, wbwShell);
		setCloseHandler(wbwModel);
		localContext.set(IShellProvider.class.getName(), new IShellProvider() {
			public Shell getShell() {
				return wbwShell;
			}
		});
		final PartServiceSaveHandler saveHandler = new PartServiceSaveHandler() {
			public Save promptToSave(MPart dirtyPart) {
				Shell shell = (Shell) context
						.get(IServiceConstants.ACTIVE_SHELL);
				Object[] elements = promptForSave(shell,
						Collections.singleton(dirtyPart));
				if (elements == null) {
					return Save.CANCEL;
				}
				return elements.length == 0 ? Save.NO : Save.YES;
			}

			public Save[] promptToSave(Collection<MPart> dirtyParts) {
				List<MPart> parts = new ArrayList<MPart>(dirtyParts);
				Shell shell = (Shell) context
						.get(IServiceConstants.ACTIVE_SHELL);
				Save[] response = new Save[dirtyParts.size()];
				Object[] elements = promptForSave(shell, parts);
				if (elements == null) {
					Arrays.fill(response, Save.CANCEL);
				} else {
					Arrays.fill(response, Save.NO);
					for (int i = 0; i < elements.length; i++) {
						response[parts.indexOf(elements[i])] = Save.YES;
					}
				}
				return response;
			}
		};
		saveHandler.logger = logger;
		localContext.set(ISaveHandler.class, saveHandler);

		if (wbwModel.getLabel() != null)
			wbwShell.setText(wbwModel.getLocalizedLabel());

		if (wbwModel.getIconURI() != null && wbwModel.getIconURI().length() > 0) {
			wbwShell.setImage(getImage(wbwModel));
		} else {
			// TODO: This should be added to the model, see bug 308494
			// it allows for a range of icon sizes that the platform gets to
			// choose from
			wbwShell.setImages(Window.getDefaultImages());
		}

	 */	
	return wbwStage;
	}
}
