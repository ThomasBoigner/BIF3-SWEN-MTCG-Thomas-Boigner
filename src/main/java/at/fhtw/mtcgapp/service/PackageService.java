package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.service.command.CreateCardCommand;
import at.fhtw.mtcgapp.service.dto.PackageDto;

import java.util.List;

public interface PackageService {
    PackageDto createPackage(String authToken, List<CreateCardCommand> commands);
    void acquirePackage(String authToken);
}
