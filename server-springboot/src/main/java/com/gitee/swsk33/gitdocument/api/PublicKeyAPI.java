package com.gitee.swsk33.gitdocument.api;

import com.gitee.swsk33.gitdocument.dataobject.PublicKey;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.service.PublicKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/public-key")
public class PublicKeyAPI {

	@Autowired
	private PublicKeyService publicKeyService;

	@PostMapping("/add")
	public Result<Void> add(@Valid @RequestBody PublicKey publicKey, BindingResult errors) throws Exception {
		if (errors.hasErrors()) {
			return Result.resultFailed(errors.getFieldError().getDefaultMessage());
		}
		return publicKeyService.add(publicKey);
	}

	@DeleteMapping("/delete/{id}")
	public Result<Void> delete(@PathVariable int id) throws Exception {
		return publicKeyService.delete(id);
	}

	@GetMapping("/get-by-user")
	public Result<List<PublicKey>> getByUser() throws Exception {
		return publicKeyService.getByUser();
	}

}