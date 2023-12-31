package it.univaq.disim.oop.noleggioveicoliunivaq.domain;

import java.util.HashSet;
import java.util.Set;

public class Tipologia {

	private Integer id;
	private String nome;

	private Set<Veicolo> veicoli = new HashSet<>();

	public Tipologia(String nome, Integer id) {
		this.id = id;
		this.nome = nome;
	}

	public Tipologia() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Set<Veicolo> getVeicoli() {
		return this.veicoli;
	}

	public void addVeicolo(Veicolo veicolo) {
		this.veicoli.add(veicolo);
	}

}
