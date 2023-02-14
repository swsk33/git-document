package com.gitee.swsk33.gitdocument.api;

import com.gitee.swsk33.gitdocument.dataobject.Anthology;
import com.gitee.swsk33.gitdocument.model.CommitInfo;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.param.ValidationRules;
import com.gitee.swsk33.gitdocument.service.AnthologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/anthology")
public class AnthologyAPI {

	@Autowired
	private AnthologyService anthologyService;

	@PostMapping("/add")
	public Result<Anthology> add(@Validated(ValidationRules.DataAdd.class) @RequestBody Anthology anthology, BindingResult errors) {
		if (errors.hasErrors()) {
			Result<Anthology> result = new Result<>();
			result.setResultFailed(errors.getFieldError().getDefaultMessage());
			return result;
		}
		return anthologyService.add(anthology);
	}

	@DeleteMapping("/delete/{id}")
	public Result<Anthology> delete(@PathVariable long id) throws IOException {
		return anthologyService.delete(id);
	}

	@PutMapping("/update")
	public Result<Anthology> update(@Validated(ValidationRules.DataUpdate.class) @RequestBody Anthology anthology, BindingResult errors) throws Exception {
		if (errors.hasErrors()) {
			Result<Anthology> result = new Result<>();
			result.setResultFailed(errors.getFieldError().getDefaultMessage());
			return result;
		}
		return anthologyService.update(anthology);
	}

	@GetMapping("/get/{id}")
	public Result<Anthology> getById(@PathVariable long id) throws Exception {
		return anthologyService.getById(id);
	}

	@GetMapping("/get-all-commits/{id}")
	public Result<List<CommitInfo>> getAllCommits(@PathVariable long id) throws Exception {
		return anthologyService.getAllCommits(id);
	}

	@GetMapping("/get-all")
	public Result<List<Anthology>> getAll() {
		return anthologyService.getAll();
	}

	@GetMapping(value = "/get-image/id/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getImage(@PathVariable long id, @RequestParam("path") String path) {
		Result<byte[]> result = anthologyService.getImageData(id, path);
		if (!result.isSuccess()) {
			return null;
		}
		return result.getData();
	}

}