package com.bellini.cursomc.services;

import com.bellini.cursomc.domain.Categoria;
import com.bellini.cursomc.domain.Produto;
import com.bellini.cursomc.repositories.CategoriaRepository;
import com.bellini.cursomc.repositories.ProdutoRepository;
import com.bellini.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repoProduto;
    @Autowired
    private CategoriaRepository repoCategoria;

    public Produto find(Integer id){
        Optional<Produto> obj = repoProduto.findById(id);

        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id
                + ", Tipo: " + Produto.class.getName()));
    }

    public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy, String direction){
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        List<Categoria> categorias = repoCategoria.findAllById(ids);
        return repoProduto.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageRequest);
    }
}
