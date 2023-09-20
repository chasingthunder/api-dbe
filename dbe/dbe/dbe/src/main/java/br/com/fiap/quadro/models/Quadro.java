package br.com.fiap.quadro.models;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;

import br.com.fiap.quadro.controllers.QuadroController;
import br.com.fiap.quadro.controllers.UsuarioController;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quadro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 1, max = 50)
    private String titulo;

    @NotBlank
    private String nota;

    @NotBlank
    private String cor;

    @NotNull
    private LocalDate data;

    @ManyToOne
    private Usuario usuario;

    public EntityModel<Quadro> toModel() {
        return EntityModel.of(
            this,
            linkTo(methodOn(QuadroController.class).show(id)).withSelfRel(),
            linkTo(methodOn(QuadroController.class).destroy(id)).withRel("delete"),
            linkTo(methodOn(UsuarioController.class).show(usuario.getId())).withRel("usuario"),
            linkTo(methodOn(QuadroController.class).index(null, Pageable.unpaged())).withRel("listAll")
        );
    }
}