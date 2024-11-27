package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.service.command.CreateCardCommand;
import at.fhtw.mtcgapp.service.dto.PackageDto;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RequiredArgsConstructor

@Slf4j
public class PackageService {
    private final Validator validator;

    public PackageDto createPackage(List<CreateCardCommand> command) {
        throw new UnsupportedOperationException();
    }
}
