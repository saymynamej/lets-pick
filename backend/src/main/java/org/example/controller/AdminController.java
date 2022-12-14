package org.example.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.example.dto.ContentDTO;
import org.example.exception.ContentSaveException;
import org.example.mapper.ContentMapper;
import org.example.model.Content;
import org.example.service.ContentService;
import org.example.validator.ContentValidator;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.util.Utils.checkCookiesNull;
import static org.example.util.Utils.writeErrorMessage;

@AllArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ContentMapper mapper;
    private final ContentValidator validator;
    private final ContentService contentService;

    @PostMapping
    public ContentDTO save(@RequestBody @Valid ContentDTO contentDTO, BindingResult bindingResult
            , HttpServletRequest request) {

        checkCookiesNull(request);
        Content content = mapper.toEntity(contentDTO);
        validator.validate(content, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new ContentSaveException(writeErrorMessage(bindingResult));
        }
        return mapper.toDTO(contentService.save(content));
    }

    @GetMapping
    public List<ContentDTO> index() {
        return mapper.toListDto(contentService.getAll());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        contentService.delete(id);
    }
}
