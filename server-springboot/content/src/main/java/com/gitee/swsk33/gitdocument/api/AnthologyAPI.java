package com.gitee.swsk33.gitdocument.api;

import cn.hutool.core.io.FileUtil;
import com.gitee.swsk33.gitdocument.dataobject.Anthology;
import com.gitee.swsk33.gitdocument.model.CommitRecord;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.param.ValidationRules;
import com.gitee.swsk33.gitdocument.service.AnthologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/anthology")
public class AnthologyAPI {

	@Autowired
	private AnthologyService anthologyService;

	@PostMapping("/add")
	public Result<Void> add(@Validated(ValidationRules.DataAdd.class) @RequestBody Anthology anthology, BindingResult errors) {
		if (errors.hasErrors()) {
			return Result.resultFailed(errors.getFieldError().getDefaultMessage());
		}
		return anthologyService.add(anthology);
	}

	@DeleteMapping("/delete/{id}")
	public Result<Void> delete(@PathVariable long id) {
		return anthologyService.delete(id);
	}

	@PatchMapping("/update")
	public Result<Void> update(@Validated(ValidationRules.DataUpdate.class) @RequestBody Anthology anthology, BindingResult errors) throws Exception {
		if (errors.hasErrors()) {
			return Result.resultFailed(errors.getFieldError().getDefaultMessage());
		}
		return anthologyService.update(anthology);
	}

	@GetMapping("/get/{id}")
	public Result<Anthology> getById(@PathVariable long id) {
		return anthologyService.getById(id);
	}

	@GetMapping("/get-all-commits/{id}")
	public Result<List<CommitRecord>> getAllCommits(@PathVariable long id) {
		return anthologyService.getAllCommits(id);
	}

	@GetMapping("/get-all")
	public Result<List<Anthology>> getAll() {
		return anthologyService.getAll();
	}

	@GetMapping(value = "/get-image/id/{id}")
	public ResponseEntity<byte[]> getImage(@PathVariable long id, @RequestParam("path") String path) {
		Result<byte[]> result = anthologyService.getImageData(id, path);
		if (!result.isSuccess()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(FileUtil.getMimeType(path))).body(result.getData());
	}

	@GetMapping("/get-not-in-database")
	public Result<List<Anthology>> getNotInDB() {
		return anthologyService.getAnthologyNotInDatabase();
	}

	@PostMapping("/restore-not-in-database")
	public Result<Void> batchAdd(@Validated(ValidationRules.DataAdd.class) @RequestBody List<Anthology> anthologies) {
		return anthologyService.restoreAnthologyNotInDatabase(anthologies);
	}

}