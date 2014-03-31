package de.fsch.ibot.ui.renderers.javafx;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import javafx.stage.Stage; 

public class WBWRenderer extends JavaFXPartRenderer
{
private static String ShellMinimizedTag = "shellMinimized"; //$NON-NLS-1$
private static String ShellMaximizedTag = "shellMaximized"; //$NON-NLS-1$

	public WBWRenderer()
	{
		// TODO Auto-generated constructor stub
	}
	
	/*
	 * Hier wird die SWT-Shell initialisiert.
	 * Ein JavaFX Renderer muss an dieser Stelle die Primary Stage initialisieren
	 *  
	 */
	public Object createWidget(MUIElement element, Object parent) 
	{
	Stage primaryStage;

		// TODO: Die Prüfung: && !(parent instanceof Control) habe ich rausgenommen
		if (!(element instanceof MWindow) || (parent != null))
		{
		return null;	
		}

	MWindow wbwModel = (MWindow) element;

	MApplication appModel = wbwModel.getContext().get(MApplication.class);
	
	/*
	 * Verstehe ich noch nicht
	Boolean rtlMode = (Boolean) appModel.getTransientData().get(E4Workbench.RTL_MODE);
	int rtlStyle = (rtlMode != null && rtlMode.booleanValue()) ? SWT.RIGHT_TO_LEFT : 0;
	 * 
	 */

	/*
		Shell parentShell = parent == null ? null : ((Control) parent)
				.getShell();

		final Shell wbwShell;
		if (parentShell == null) {
			wbwShell = new Shell(Display.getCurrent(), SWT.SHELL_TRIM
					| rtlStyle);
			wbwModel.getTags().add("topLevel"); //$NON-NLS-1$
		} else if (wbwModel.getTags().contains("dragHost")) { //$NON-NLS-1$
			wbwShell = new Shell(parentShell, SWT.BORDER | rtlStyle);
			wbwShell.setAlpha(110);
		} else {
			wbwShell = new Shell(parentShell, SWT.TITLE | SWT.RESIZE | SWT.MAX
					| SWT.CLOSE | rtlStyle);

			// Prevent ESC from closing the DW
			wbwShell.addTraverseListener(new TraverseListener() {
				public void keyTraversed(TraverseEvent e) {
					if (e.detail == SWT.TRAVERSE_ESCAPE) {
						e.doit = false;
					}
				}
			});
		}

		wbwShell.setBackgroundMode(SWT.INHERIT_DEFAULT);

		Rectangle modelBounds = wbwShell.getBounds();
		if (wbwModel instanceof EObject) {
			EObject wbw = (EObject) wbwModel;
			EClass wbwclass = wbw.eClass();
			// use eIsSet rather than embed sentinel values
			if (wbw.eIsSet(wbwclass.getEStructuralFeature("x"))) { //$NON-NLS-1$
				modelBounds.x = wbwModel.getX();
			}
			if (wbw.eIsSet(wbwclass.getEStructuralFeature("y"))) { //$NON-NLS-1$
				modelBounds.y = wbwModel.getY();
			}
			if (wbw.eIsSet(wbwclass.getEStructuralFeature("height"))) { //$NON-NLS-1$
				modelBounds.height = wbwModel.getHeight();
			}
			if (wbw.eIsSet(wbwclass.getEStructuralFeature("width"))) { //$NON-NLS-1$
				modelBounds.width = wbwModel.getWidth();
			}
		}
		// Force the shell onto the display if it would be invisible otherwise
		Rectangle displayBounds = Display.getCurrent().getBounds();
		if (!modelBounds.intersects(displayBounds)) {
			Rectangle clientArea = Display.getCurrent().getClientArea();
			modelBounds.x = clientArea.x;
			modelBounds.y = clientArea.y;
		}
		wbwShell.setBounds(modelBounds);

		setCSSInfo(wbwModel, wbwShell);

		// set up context
		IEclipseContext localContext = getContext(wbwModel);

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
	return newWidget;
	}
}
