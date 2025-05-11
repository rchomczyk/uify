package dev.shiza.uify.canvases.consume;

public interface ConsumingCanvasBehaviours {

    ConsumingCanvas onConsumption(final ConsumptionBehaviour<ConsumptionResult> consumptionBehaviour);

    ConsumingCanvas onIndexedConsumption(final ConsumptionBehaviour<IndexedConsumptionResult> consumptionBehaviour);
}
