package dev.shiza.uify.canvases.paginated;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

final class PaginatedCanvasUtils {

    private PaginatedCanvasUtils() {}

    static <T> List<List<T>> partition(final Collection<T> collection, final int elementsPerPartition) {
        if (collection == null || elementsPerPartition <= 0) {
            throw new PaginatedCanvasPartitioningException(
                "Collection cannot be null and elements per partition must be greater than 0");
        }

        final List<T> list = new ArrayList<>(collection);
        return IntStream.range(0, (int) Math.ceil((double) list.size() / elementsPerPartition))
            .mapToObj(index -> list.subList(
                index * elementsPerPartition,
                Math.min((index + 1) * elementsPerPartition, list.size())))
            .collect(Collectors.toCollection(ArrayList::new));
    }
}
