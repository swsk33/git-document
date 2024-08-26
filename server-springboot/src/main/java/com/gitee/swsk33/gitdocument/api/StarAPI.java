package com.gitee.swsk33.gitdocument.api;

import com.gitee.swsk33.gitdocument.dataobject.Star;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.param.ValidationRules;
import com.gitee.swsk33.gitdocument.service.StarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/star")
public class StarAPI {

	@Autowired
	private StarService starService;

	@PostMapping("/add")
	public Result<Star> add(@Validated({ValidationRules.DataAdd.class}) @RequestBody Star star, BindingResult errors) {
		if (errors.hasErrors()) {
			return Result.resultFailed(errors.getFieldError().getDefaultMessage());
		}
		return starService.add(star);
	}

	@DeleteMapping("/delete/{id}")
	public Result<Void> delete(@PathVariable long id) {
		return starService.delete(id);
	}

	@GetMapping("/get-star-count/{anthologyId}")
	public Result<Integer> getAnthologyStarCount(@PathVariable long anthologyId) {
		return starService.getAnthologyStarCount(anthologyId);
	}

}