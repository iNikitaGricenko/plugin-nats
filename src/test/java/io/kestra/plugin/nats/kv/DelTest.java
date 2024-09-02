package io.kestra.plugin.nats.kv;

import io.kestra.core.junit.annotations.KestraTest;
import io.kestra.core.runners.RunContextFactory;
import io.kestra.core.utils.IdUtils;
import jakarta.inject.Inject;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@KestraTest
public class DelTest {

	@Inject
	protected RunContextFactory runContextFactory;

	@Test
	public void deletePair() throws Exception {
		String bucket = createBucket();
		Map<String, Object> keyValuePair = putPair(bucket);
		List<String> keys = new ArrayList<>(keyValuePair.keySet());

		Del.builder()
			.url("localhost:4222")
			.username("kestra")
			.password("k3stra")
			.bucketName(bucket)
			.keys(
				keys
			)
			.build()
			.run(runContextFactory.of());

		Get.Output getOutput = Get.builder()
			.url("localhost:4222")
			.username("kestra")
			.password("k3stra")
			.bucketName(bucket)
			.keys(
				new ArrayList<>(keyValuePair.keySet())
			)
			.build()
			.run(runContextFactory.of());

		assertThat(getOutput.getOutput(), anEmptyMap());
	}

	public Map<String, Object> putPair(String bucket) throws Exception {
		Map<String, Object> keyValuePair = Map.of(
			"key1", "value1",
			"key2", "value2",
			"key3", "3"
		);

		Put.builder()
			.url("localhost:4222")
			.username("kestra")
			.password("k3stra")
			.bucketName(bucket)
			.values(
				keyValuePair
			)
			.build()
			.run(runContextFactory.of());

		return keyValuePair;
	}

	public String createBucket() throws Exception {
		CreateBucket.Output bucketOutput = CreateBucket.builder()
			.url("localhost:4222")
			.username("kestra")
			.password("k3stra")
			.name(IdUtils.create())
			.build()
			.run(runContextFactory.of());

		return bucketOutput.getBucket();
	}

}
