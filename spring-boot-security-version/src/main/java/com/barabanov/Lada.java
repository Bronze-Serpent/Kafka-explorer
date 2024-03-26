package com.barabanov;

import java.util.UUID;

public record Lada(UUID uuid, String name, Attempt attempt)
{
}
