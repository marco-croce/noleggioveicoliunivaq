package it.univaq.disim.oop.noleggioveicoliunivaq.view;

import it.univaq.disim.oop.noleggioveicoliunivaq.controller.DoubleDataInitializable;
import javafx.scene.Parent;

public class ViewDouble<T, S> {

	private Parent view;
	private DoubleDataInitializable<T, S> doubleController;

	public ViewDouble(Parent view, DoubleDataInitializable<T, S> doubleController) {
		super();
		this.view = view;
		this.doubleController = doubleController;
	}

	public Parent getView() {
		return view;
	}

	public void setView(Parent view) {
		this.view = view;
	}

	public DoubleDataInitializable<T, S> getDoubleController() {
		return doubleController;
	}

	public void setController(DoubleDataInitializable<T, S> doubleController) {
		this.doubleController = doubleController;
	}

}
