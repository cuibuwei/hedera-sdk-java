/*-
 *
 * Hedera Java SDK
 *
 * Copyright (C) 2024 Hedera Hashgraph, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

plugins {
    id("com.hedera.gradle.examples.java")
}

mainModuleInfo {
    runtimeOnly("io.grpc.netty.shaded")
    runtimeOnly("org.slf4j.simple")
}

dependencies.constraints {
    implementation("com.hedera.hashgraph:sdk:2.45.0")
    implementation("com.hedera.hashgraph:sdk-full:2.45.0")
}
