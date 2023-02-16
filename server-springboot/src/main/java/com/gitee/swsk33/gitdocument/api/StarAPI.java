package com.gitee.swsk33.gitdocument.api;

import com.gitee.swsk33.gitdocument.dataobject.Star;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.service.StarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/star")
public class StarAPI {

	@Autowired
	private StarService starService;

	@PostMapping("/add")
	public Result<Void> add(@Valid @RequestBody Star star, BindingResult errors) {
		if (errors.hasErrors()) {
			Result<Void> result = new Result<>();
			result.setResultFailed(errors.getFieldError().getDefaultMessage());
			return result;
		}
		return starService.add(star);
	}

	@DeleteMapping("/delete/{id}")
	public Result<Void> delete(@PathVariable long id) {
		return starService.delete(id);
	}

	@GetMapping("/get-by-user")
	public Result<List<Star>> getByUser() {
		return starService.getByUser();
	}

	@GetMapping("/get-star-count/{anthologyId}")
	public Result<Integer> getAnthologyStarCount(@PathVariable long anthologyId) {
		return starService.getAnthologyStarCount(anthologyId);
	}

}