package it.univaq.disim.oop.noleggioveicoliunivaq.domain;

public class Tariffa {

	private Integer id;
	private Double prezzo;
	private Categoria categoria;
	private Veicolo veicolo;

	public Tariffa(Double prezzo, Categoria categoria, Integer id) {
		this.id = id;
		this.prezzo = prezzo;
		this.categoria = categoria;
	}

	public Tariffa() {

	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getPrezzo() {
		return this.prezzo;
	}

	public void setPrezzo(Double prezzo) {
		this.prezzo = prezzo;
	}

	public Categoria getCategoria() {
		return this.categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Veicolo getVeicolo() {
		return this.veicolo;
	}

	public void setVeicolo(Veicolo veicolo) {
		this.veicolo = veicolo;
	}

}
