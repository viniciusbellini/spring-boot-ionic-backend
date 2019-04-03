package com.bellini.cursomc.services.validation;

import com.bellini.cursomc.domain.Cliente;
import com.bellini.cursomc.domain.enums.TipoCliente;
import com.bellini.cursomc.dto.ClienteNewDTO;
import com.bellini.cursomc.repositories.ClienteRepository;
import com.bellini.cursomc.resources.exceptions.FieldMessage;
import com.bellini.cursomc.services.validation.utils.BR;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class ClienteInsertValidator implements ConstraintValidator <ClienteInsert, ClienteNewDTO> {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public void initialize(ClienteInsert constraintAnnotation) {
    }

    @Override
    public boolean isValid(ClienteNewDTO objDTO, ConstraintValidatorContext context) {
        List<FieldMessage> list = new ArrayList<>();

        if (objDTO.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidCPF(objDTO.getCpfOuCnpj())){
            list.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
        }

        if (objDTO.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidCNPJ(objDTO.getCpfOuCnpj())){
            list.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));
        }

        Cliente aux = clienteRepository.findByEmail(objDTO.getEmail());
        if (aux != null){
            list.add(new FieldMessage("email", "Email já existente"));
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName()).addConstraintViolation();
        }

        return list.isEmpty();
    }
}
